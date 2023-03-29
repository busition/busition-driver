package com.busition.driver.Gps

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.annotation.NonNull
import com.busition.driver.main_screen.FragmentItemDetailScreen
import com.busition.driver.placeholder.PlaceholderContent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import kotlin.math.*


class LocationTraker(context: Context, googleMap: GoogleMap,
                     private var locList: MutableList<LatLng>
) : LocationListener {
    private var mContext: Context = context
    private val r = 6372.8 * 1000
    private var gmap: GoogleMap = googleMap
    private var checkGps: Boolean = false
    private var checkNetwork: Boolean = false
    private var canGetLocation: Boolean = true
    private var nowLocationIndex:Int = 0
    var nowLocation:LatLng = LatLng(0.0,0.0)
    private  var currentLocationMarker: Marker = gmap.addMarker(
        MarkerOptions()
            .position(nowLocation)
            .title("tmp")
    )!!
    private var errorMsg: String = ""
    lateinit var locationManager: LocationManager

    init {
        while (locList == null) {
            Log.d("gps", "null")
        }
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


//            if (!checkNetwork) {
//                errorMsg += "\nError for network"
//            } else {
//                locationManager.requestLocationUpdates(
//                    LocationManager.NETWORK_PROVIDER,
//                    1000L,
//                    20f, this
//                )
//            }
        }

    }

    override fun onLocationChanged(location: Location) {
//        Log.d("gps", location.latitude.toString() + " " + location.longitude.toString())
        updateLocationIndex()
        updateGmapPolyline(location)
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



    fun updateGmapPolyline(location: Location){
        gmap.clear()
        Log.d("gps", nowLocation.toString())
        nowLocation = LatLng(location.latitude, location.longitude)

        currentLocationMarker.remove()
        currentLocationMarker = gmap.addMarker(
            MarkerOptions()
                .position(nowLocation)
                .title("tmp")
        )!!
        var list:MutableList<LatLng> = arrayListOf()
        var tmp:LatLng = LatLng(0.0,0.0)

        PlaceholderContent.ITEMS.forEach {
            tmp = LatLng(it.station_latitude, it.station_longitude)
            FragmentItemDetailScreen.gmap?.addMarker(
                MarkerOptions()
                    .position(tmp)
                    .title(it.station_name)
            )
        }
        list.add(nowLocation)
        for (i in nowLocationIndex until locList.size) {
            list.add(locList[i])
        }
        gmap.addPolyline(
            PolylineOptions()
                .addAll(list)
                .width(10f)
                .color(Color.RED)
        )
    }

    fun updateLocationIndex(){

        var tmp:Int = 0
        var tt:Int = 0
        for (i in nowLocationIndex until locList.size){
            tt = getDistance(nowLocation.latitude,nowLocation.longitude, locList[i].latitude, locList[i].longitude)

            if (tmp == 0){
                tmp = tt
            }else{
                if(tmp<tt){
                    nowLocationIndex = i
                    Log.d("meter", i.toString())
                    break
                }
            }
        }
    }

    fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Int {
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2.0) + sin(dLon / 2).pow(2.0) * cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2))
        val c = 2 * asin(sqrt(a))
        return (r * c).toInt()
    }

    fun finish(){
        locationManager.removeUpdates(LocationTraker(mContext, gmap, locList))
        locationManager.removeUpdates(this)

    }
}