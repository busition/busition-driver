package com.busition.driver.main_screen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
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
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.busition.driver.FloatingService
import com.busition.driver.R
import com.busition.driver.SocketIo
import com.busition.driver.UserInfoDataFragment.Companion.userInfoRecyclerAdapter
import com.busition.driver.databinding.ActivityMainScreenBinding
import com.busition.driver.main_screen.ScheduleList.Companion.driver_uuid
import com.google.android.material.snackbar.Snackbar
import java.util.UUID

class MainScreen : AppCompatActivity() {

    private lateinit var binding: ActivityMainScreenBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var userName: String

    private var tmpBleList: MutableList<String> = ArrayList()

    private val SCAN_PERIOD: Long = 1000000
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
    fun runRecv(){
        runOnUiThread {
            SocketIo.recvLocation()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        userName = intent.getStringExtra("userName").toString()
        driver_uuid = intent.getStringExtra("driver_uuid" ).toString()
        ScheduleList.getUserData(userName, driver_uuid )

        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)

        val navController = findNavController(R.id.main_screen_nav)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)





        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "1:1 문의사항....", Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.fab)
                .setAction("Action", null).show()
        }
        if(isMyServiceRunning()){
            stopService(Intent(this, FloatingService::class.java))
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
            if (FloatingService::class.java.getName() == service.service.className) {
                return true
            }
        }
        return false
    }

    @SuppressLint("MissingPermission")
    fun scanLeDevice(enable: Boolean) {
        requestBlePermissions(this, PERMISSIONS_REQUEST_CODE)
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        Log.d("Mainscreen", bluetoothManager.toString())
        val mBluetoothAdapter = bluetoothManager.adapter
        val mBluetoothLeScanner = mBluetoothAdapter.bluetoothLeScanner
        Log.d("error", mBluetoothAdapter.toString())
        var mScanning=true
        val mHandler = Handler()
        Log.d("bleble ", mBluetoothAdapter.toString())
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed({
                mScanning = false
                mBluetoothLeScanner.stopScan(scanCallback)

            }, SCAN_PERIOD)
            mScanning = true
            mBluetoothLeScanner.startScan(scanCallback)
        } else {
            mScanning = false
            mBluetoothLeScanner.stopScan(scanCallback)
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

            Log.d("ble", "${device.name},${device.address}, $rssi, $scanRecord, $device, ${device.uuids}")
        }

    private val scanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
//            if (!tmpBleList.contains(result?.device?.uuids.toString())){
//                tmpBleList.add(result?.device?.uuids.toString())
//
//            }
            if (result?.scanRecord?.serviceUuids?.get(0).toString() == "11111111-1111-1111-1111-111111111111"){
                test_code = true
                userInfoRecyclerAdapter.notifyDataSetChanged()

            }
            if (check_uuid.contains(result?.scanRecord?.serviceUuids.toString())){
                user_uuid.add(result?.scanRecord?.serviceUuids.toString())
                check_uuid.remove(result?.scanRecord?.serviceUuids.toString())
                userInfoRecyclerAdapter.notifyDataSetChanged()
            }
            Log.d("ble instance","${result?.device?.name} ${result?.device?.uuids} ${result?.scanRecord?.serviceUuids}" )
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            Log.d("ble",results.toString())
        }

        override fun onScanFailed(errorCode: Int) {
            Log.d("ble error", errorCode.toString())
        }
    }
    companion object {
        var driver_uuid: String = ""
        var token: String = ""
        var test_code: Boolean = false
        var user_uuid: ArrayList<String> = ArrayList()
        var check_uuid: ArrayList<String> = ArrayList()
    }

}