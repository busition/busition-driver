package io.busition.driver.main_screen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.ParcelUuid
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import io.busition.driver.Service.FloatingService
import io.busition.driver.R
import io.busition.driver.Service.SocketIo
import io.busition.driver.main_screen.fragment.UserInfoDataFragment.Companion.userInfoRecyclerAdapter
import io.busition.driver.databinding.ActivityMainScreenBinding
import io.busition.driver.main_screen.fragment.ScheduleList
import com.google.android.material.snackbar.Snackbar
import io.busition.driver.main_screen.fragment.BusStartMainScreen
import io.busition.driver.main_screen.fragment.UserInfoDataFragment
import io.busition.driver.roomDB.AppDatabase
import io.busition.driver.roomDB.DataManager

class MainScreen : AppCompatActivity() {

    private lateinit var binding: ActivityMainScreenBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var userName: String
    private lateinit var mBluetoothAdapter: BluetoothAdapter
    private lateinit var mBluetoothLeScanner: BluetoothLeScanner
    private lateinit var bluetoothManager: BluetoothManager

    private var tmpBleList: MutableList<String> = ArrayList()

    private val SCAN_PERIOD: Long = 60000
    private val PERMISSIONS_REQUEST_CODE = 191

    private val BLE_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    @SuppressLint("InlinedApi")
    private val ANDROID_12_BLE_PERMISSIONS = arrayOf(
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.BLUETOOTH_ADVERTISE,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    fun runRecv() {
        runOnUiThread {
            SocketIo.recvLocation()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        userName = intent.getStringExtra("userName").toString()
        driver_uuid = intent.getStringExtra("driver_uuid").toString()
        ScheduleList.getUserData(userName, driver_uuid)

        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)
        DataManager.getInstance(this)

        BusStartMainScreen.callPassengerListListener = UserInfoDataFragment()
        bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothAdapter = bluetoothManager.adapter
        try{
            mBluetoothLeScanner = mBluetoothAdapter.bluetoothLeScanner
        }catch (e: Exception){
            Log.d("ble test", e.toString())
            Toast.makeText(this, "블루투스를 켜주세요", Toast.LENGTH_SHORT).show()
        }



        val navController = findNavController(R.id.main_screen_nav)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)



        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "1:1 문의사항....", Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.fab)
                .setAction("Action", null).show()
        }
        if (isMyServiceRunning()) {
            stopService(Intent(this, io.busition.driver.Service.FloatingService::class.java))
        }




    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.main_screen_nav)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }


    private fun isMyServiceRunning(): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (io.busition.driver.Service.FloatingService::class.java.getName() == service.service.className) {
                return true
            }
        }
        return false
    }

    @SuppressLint("MissingPermission")
    fun scanLeDevice(enable: Boolean) {
        requestBlePermissions(this, PERMISSIONS_REQUEST_CODE)
        Log.d("ble test ", mBluetoothAdapter.toString())
        val mHandler = Handler()
        try {
            if (enable && mBluetoothAdapter.isEnabled) {
                mHandler.postDelayed({
                    mBluetoothLeScanner.stopScan(scanCallback)
                    if (scanning) {
                        scanLeDevice(true)
                    } else {
                        scanLeDevice(false)
                    }
                }, SCAN_PERIOD)
                Log.d("ble log", "start scan")
                mBluetoothLeScanner.startScan(scanCallback)
            } else {
                mBluetoothLeScanner.stopScan(scanCallback)
                Log.d("ble log", "stop scan")
            }
        }catch (e: Exception){
            Log.d("ble test", e.toString())
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }


    }

    private fun buildScanSettings() = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_BALANCED).build()

    fun requestBlePermissions(activity: Activity?, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) ActivityCompat.requestPermissions(
            activity!!,
            ANDROID_12_BLE_PERMISSIONS,
            requestCode
        ) else ActivityCompat.requestPermissions(
            activity!!, BLE_PERMISSIONS, requestCode

        )
    }

    val scanFilter = ScanFilter.Builder()
        .setServiceUuid(ParcelUuid.fromString("0000FFFF-0000-1000-8000-00805F9B34FB"))
        .build()


    @SuppressLint("MissingPermission")
    private val mLeScanCallback =
        BluetoothAdapter.LeScanCallback { device, rssi, scanRecord ->

            Log.d(
                "ble",
                "${device.name},${device.address}, $rssi, $scanRecord, $device, ${device.uuids}"
            )
        }

    private val scanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
//            if (!tmpBleList.contains(result?.device?.uuids.toString())){
//                tmpBleList.add(result?.device?.uuids.toString())
//
//            }
            if (result?.scanRecord?.serviceUuids?.get(0)
                    .toString() == "11111111-1111-1111-1111-111111111111"
            ) {
                test_code = true
                userInfoRecyclerAdapter.notifyDataSetChanged()

            }
            if (result?.rssi!! > -50 && !outofbus) {
//                Log.d(
//                    "ble instance",
//                    "${result?.device?.name} ${result?.scanRecord?.serviceUuids?.get(0)} ${result?.rssi}"
//                )
                if (user_type == "normal") {
                    if (check_uuid.contains(
                            result?.scanRecord?.serviceUuids?.get(0).toString()
                        ) && !user_uuid.contains(
                            result?.scanRecord?.serviceUuids?.get(0).toString()
                        )
                    ) {
                        user_uuid.add(result?.scanRecord?.serviceUuids?.get(0).toString())
                        check_uuid.add(result?.scanRecord?.serviceUuids?.get(0).toString())
//                    check_uuid.remove(result?.scanRecord?.serviceUuids?.get(0).toString())
//                        userInfoRecyclerAdapter.notifyDataSetChanged()

                    }
                } else {
                    if (!company_check_uuid.contains(
                            result?.scanRecord?.serviceUuids?.get(0).toString()
                        )
                    ) {
                        Log.d(
                            "test socket log ",
                            result?.scanRecord?.serviceUuids?.get(0).toString()
                        )
//                        SocketIo.sendUserUuid(result?.scanRecord?.serviceUuids?.get(0).toString(),"1")
                        company_check_uuid.add(result?.scanRecord?.serviceUuids?.get(0).toString())

                    }
                }

                userInfoRecyclerAdapter.notifyDataSetChanged()
            }


        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            Log.d("ble batch scan ", results.toString())
        }

        override fun onScanFailed(errorCode: Int) {
            Log.d("ble error", errorCode.toString())
        }

    }

    companion object {
        var stop: Boolean = false
        var driver_uuid: String = ""
        var token: String = ""
        var test_code: Boolean = false
        var user_uuid: ArrayList<String> = ArrayList()
        var check_uuid: ArrayList<String> = ArrayList()
        var company_user_uuid: ArrayList<String> = ArrayList()
        var company_check_uuid: ArrayList<String> = ArrayList()
        var user_type: String = "normal"
        var scanning: Boolean = true
        var firebaseToken: String = ""
        var outofbus: Boolean = false

    }



}