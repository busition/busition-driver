package com.busition.driver.Gps

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.annotation.NonNull
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class LocationTraker(context: Context, googleMap: GoogleMap) : LocationListener {
    private var mContext: Context = context

    private var gmap: GoogleMap = googleMap
    private var checkGps: Boolean = false
    private var checkNetwork: Boolean = false
    private var canGetLocation: Boolean = true
    var nowLocation:LatLng = LatLng(0.0,0.0)
    private  var currentLocationMarker: Marker = gmap.addMarker(
        MarkerOptions()
            .position(nowLocation)
            .title("tmp")
    )!!
    private var loc: Location? = null

    private var errorMsg: String = ""
    private lateinit var locationManager: LocationManager

    init {
        getLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        checkGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        checkNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
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

    override fun onLocationChanged(@NonNull location: Location) {
//        Log.d("gps", location.latitude.toString() + " " + location.longitude.toString())
        Log.d("gps", nowLocation.toString())
        nowLocation = LatLng(location.latitude, location.longitude)

        currentLocationMarker.remove()
        currentLocationMarker = gmap.addMarker(
            MarkerOptions()
                .position(nowLocation)
                .title("tmp")
        )!!
//        val boundsBuilder = LatLngBounds.Builder()
//        boundsBuilder.include(nowLocation)
//        val bounds = boundsBuilder.build()
//        gmap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))

    }



    @Deprecated("")
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}


}