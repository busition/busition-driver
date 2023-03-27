package com.busition.driver.Ble

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.*
import android.bluetooth.BluetoothAdapter.LeScanCallback
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.HandlerCompat
import com.busition.driver.main_screen.MainScreen
import com.busition.driver.main_screen.MainScreen.Companion.bluetoothManager


class BleScanner(bluetoothAdapter: BluetoothAdapter?) {


    private val adapter = BluetoothAdapter.getDefaultAdapter()
    private val gattList: MutableList<BluetoothGatt> = ArrayList()
    private val hashDeviceMap: HashMap<String, BluetoothDevice?> = HashMap()
    private val mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper())
    private var scanning = false

    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var mScanning = false
    private var mHandler: Handler? = null

    private val REQUEST_ENABLE_BT = 1

    // Stops scanning after 10 seconds.
    private val SCAN_PERIOD: Long = 10000

    /**
     * This block is for requesting permissions up to Android 12+
     *
     */
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

    fun requestBlePermissions(activity: Activity?, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) ActivityCompat.requestPermissions(
            activity!!,
            ANDROID_12_BLE_PERMISSIONS,
            requestCode
        ) else ActivityCompat.requestPermissions(
            activity!!, BLE_PERMISSIONS, requestCode
        )
    }

    fun init(activity: MainScreen) {
        mHandler = Handler()
        requestBlePermissions(activity, PERMISSIONS_REQUEST_CODE)

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.

        mBluetoothAdapter = bluetoothManager?.adapter
        Log.d("bleble ", mBluetoothAdapter.toString())
    }

    @SuppressLint("MissingPermission")
     fun scanLeDevice(enable: Boolean) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler!!.postDelayed({
                mScanning = false
                mBluetoothAdapter!!.stopLeScan(mLeScanCallback)

            }, SCAN_PERIOD)
            mScanning = true
            mBluetoothAdapter!!.startLeScan(mLeScanCallback)
        } else {
            mScanning = false
            mBluetoothAdapter!!.stopLeScan(mLeScanCallback)
        }

    }


    // Device scan callback.
    private val mLeScanCallback =
        LeScanCallback { device, rssi, scanRecord ->
            Log.d("ble", "$device, $rssi, $scanRecord")
        }
}