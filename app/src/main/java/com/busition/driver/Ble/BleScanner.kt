package com.busition.driver.Ble

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.HandlerCompat


class BleScanner(bluetoothAdapter: BluetoothAdapter) {

}
class BluetoothGuide {
    private val adapter = BluetoothAdapter.getDefaultAdapter()
    private val gattList: MutableList<BluetoothGatt> = ArrayList()
    private val hashDeviceMap: HashMap<String, BluetoothDevice?> = HashMap()
    private val mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper())
    private var scanning = false

    /**
     * System Bluetooth On Check
     */
    val isOn: Boolean
        get() = adapter.isEnabled

    /**
     * System Bluetooth On
     */
    @SuppressLint("MissingPermission")
    fun on(activity: AppCompatActivity) {
        if (!adapter.isEnabled) {
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            activity.startActivityForResult(intent, INTENT_REQUEST_BLUETOOTH_ENABLE)
        }
    }

    /**
     * System Bluetooth On Result
     */
    fun onActivityResult(requestCode: Int, resultCode: Int): Boolean {
        return (requestCode == INTENT_REQUEST_BLUETOOTH_ENABLE
                && Activity.RESULT_OK == resultCode)
    }

    /**
     * System Bluetooth Off
     */
    @SuppressLint("MissingPermission")
    fun off() {
        if (adapter.isEnabled) adapter.disable()
    }

    /**
     * Check model for ScanRecodeData
     */
    interface OnCheckModelListener {
        fun isChecked(bytes: ByteArray?): Boolean
        fun scannedDevice(result: ScanResult?)
    }

    private var onCheckModelListener: OnCheckModelListener? = null
    fun setOnCheckModelListener(onCheckModelListener: OnCheckModelListener?): BluetoothGuide {
        this.onCheckModelListener = onCheckModelListener
        return this
    }

    private val callback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            val serviceDataMap = result.scanRecord!!
                .serviceData ?: return
            if (onCheckModelListener == null) return
            for (parcelUuid in serviceDataMap.keys) {
                if (onCheckModelListener!!.isChecked(result.scanRecord!!.getServiceData(parcelUuid))) {
                    if (!hasDevice(result.device.toString())) {
                        addDevice(result.device.address, result.device)
                        if (onCheckModelListener != null) {
                            onCheckModelListener!!.scannedDevice(result)
                        }
                    }
                    break
                }
            }
        }
    }

    /**
     * DO NOT CONNECT DEVICE
     */
    private fun addDevice(address: String, device: BluetoothDevice) {
        hashDeviceMap[address] = device
    }

    /**
     * DO NOT CONNECT DEVICE
     */
    private fun hasDevice(address: String): Boolean {
        return hashDeviceMap[address] != null
    }

    /**
     * DO NOT CONNECT DEVICE
     */
    fun onComplete() {
        hashDeviceMap.clear()
    }

    /**
     * Start Scan
     */
    @SuppressLint("MissingPermission")
    fun scanDevices() {
        if (!adapter.isEnabled) return
        if (scanning) return
        val scanner = adapter.bluetoothLeScanner
        mainThreadHandler.postDelayed({
            scanning = false
            scanner.stopScan(callback)
        }, (2 * 60 * 1000).toLong())
        scanning = true
        scanner.startScan(callback)
    }


    private val gattCallback: BluetoothGattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            if (status == BluetoothGatt.GATT_FAILURE) {
                gatt.disconnect()
                gatt.close()
                hashDeviceMap.remove(gatt.device.address)
                return
            }
            if (status == 133) // Unknown Error
            {
                gatt.disconnect()
                gatt.close()
                hashDeviceMap.remove(gatt.device.address)
                return
            }
            if (newState == BluetoothGatt.STATE_CONNECTED && status == BluetoothGatt.GATT_SUCCESS) {
                // "Connected to " + gatt.getDevice().getName()
                gatt.discoverServices()
            }
        }

        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            super.onServicesDiscovered(gatt, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                val services = gatt.services
                for (service in services) {
                    // "Found service : " + service.getUuid()
                    for (characteristic in service.characteristics) {
                        //"Found characteristic : " + characteristic.getUuid()
                        if (hasProperty(
                                characteristic,
                                BluetoothGattCharacteristic.PROPERTY_READ
                            )
                        ) {
                            // "Read characteristic : " + characteristic.getUuid());
                            gatt.readCharacteristic(characteristic)
                        }
                        if (hasProperty(
                                characteristic,
                                BluetoothGattCharacteristic.PROPERTY_NOTIFY
                            )
                        ) {
                            // "Register notification for characteristic : " + characteristic.getUuid());
                            gatt.setCharacteristicNotification(characteristic, true)
                        }
                    }
                }
            }
        }

    }

    fun hasProperty(characteristic: BluetoothGattCharacteristic, property: Int): Boolean {
        val prop = characteristic.properties and property
        return prop == property
    }

    interface OnNotifyValueListener<T> {
        fun onValue(deivce: BluetoothDevice?, value: T)
        fun formatter(characteristic: BluetoothGattCharacteristic?): T
    }

    interface OnReadValueListener<T> {
        fun onValue(deivce: BluetoothDevice?, value: T)
        fun formatter(characteristic: BluetoothGattCharacteristic?): T
    }

    private var onNotifyValueListener: OnNotifyValueListener<*>? = null
    fun setOnNotifyValueListener(onNotifyValueListener: OnNotifyValueListener<*>?): BluetoothGuide {
        this.onNotifyValueListener = onNotifyValueListener
        return this
    }

    private var onReadValueListener: OnReadValueListener<*>? = null
    fun setOnReadValueListener(onReadValueListener: OnReadValueListener<*>?): BluetoothGuide {
        this.onReadValueListener = onReadValueListener
        return this
    }

    companion object {
        const val INTENT_REQUEST_BLUETOOTH_ENABLE = 0x0701
    }
}