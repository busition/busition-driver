package com.example.busition_driver.Gps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.busition_driver.MainActivity
import com.example.busition_driver.main_screen.MainScreen
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class LocationTraker(context: Context, activity: MainScreen, googleMap: GoogleMap) : LocationListener {
    private var mContext: Context = context
    private var MainActivity: MainScreen = activity
    private var gmap: GoogleMap = googleMap
    private var checkGps: Boolean = false
    private var checkNetwork: Boolean = false
    private var canGetLocation: Boolean = false
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private var loc: Location? = null

    private var errorMsg: String = ""
    private lateinit var locationManager: LocationManager

    init {
        getLocation()
    }

    private fun getLocation() {
        locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        checkGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        checkNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                MainActivity,
                arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                ),
                1
            )
        } else {
            canGetLocation = true
        }
        if (canGetLocation) {
            if (!checkGps) {
                errorMsg = "Error for gps module"
            } else {

                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000L,
                    20f, this
                )
            }


            if (!checkNetwork) {
                errorMsg += "\nError for network"
            } else {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    1000L,
                    20f, this
                )
            }
        }

    }

    override fun onLocationChanged(location: Location) {
        Log.d("gps", location.latitude.toString() + " " + location.longitude.toString())
        val tmp = LatLng(location.latitude, location.longitude)
        gmap.addMarker(
            MarkerOptions()
                .position(tmp)
                .title("tmp")

        )
        val cameraPosition = CameraPosition.Builder()
            .target(tmp)
            .zoom(15f)
            .build()
        gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}


}