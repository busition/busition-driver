package io.busition.driver.Service.Gps

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import io.busition.driver.R
import io.busition.driver.Service.SocketIo
import io.busition.driver.main_screen.fragment.BusStartMainScreen
import io.busition.driver.main_screen.dataholder.StationDataContent
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
            .icon(BitmapFromVector(context, R.drawable.bus_icon))
            .position(nowLocation)
            .title("현재 위치")
    )!!
    private var errorMsg: String = ""
    lateinit var locationManager: LocationManager
    lateinit var locationCallback: LocationCallback
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext)

    init {
        getLocation()

    }

    private fun BitmapFromVector(context:Context,vectorResId:Int): BitmapDescriptor? {
        //drawable generator
        var vectorDrawable: Drawable
        vectorDrawable= ContextCompat.getDrawable(context,vectorResId)!!
        vectorDrawable.setBounds(0,0,vectorDrawable.intrinsicWidth,vectorDrawable.intrinsicHeight)
        //bitmap genarator
        var bitmap:Bitmap
        bitmap= Bitmap.createBitmap(vectorDrawable.intrinsicWidth,vectorDrawable.intrinsicHeight,Bitmap.Config.ARGB_8888)
        //canvas genaret
        var canvas:Canvas
        //pass bitmap in canvas constructor
        canvas= Canvas(bitmap)
        //pass canvas in drawable
        vectorDrawable.draw(canvas)
        //return BitmapDescriptorFactory
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    @SuppressLint("MissingPermission")
//    private fun getLocation() {
//        locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        checkGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//        checkNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
//        if (canGetLocation) {
//            if (!checkGps) {
//                errorMsg = "Error for gps module"
//                Toast.makeText(mContext, errorMsg, Toast.LENGTH_LONG).show()
//            } else {
//                locationManager.requestLocationUpdates(
//                    LocationManager.GPS_PROVIDER,
//                    1000L,
//                    20f, this
//
//                )
//            }
////            if (!checkNetwork) {
////                errorMsg += "\nError for network"
////            } else {
////                locationManager.requestLocationUpdates(
////                    LocationManager.NETWORK_PROVIDER,
////                    1000L,
////                    20f, this
////                )
////            }
//        }
//
//    }

    private fun getLocation(){
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                Log.d("location changed", p0.lastLocation?.latitude.toString() + " " + p0.lastLocation?.longitude.toString())
                StationDataContent.checkStation(p0.lastLocation?.latitude!!,  p0.lastLocation?.longitude!!)
                val location = LatLng(p0.lastLocation?.latitude!!, p0.lastLocation?.longitude!!)
                updateGmapPolyline(location, p0.lastLocation?.speed!!)
                SocketIo.sendLocation(location.latitude,location.longitude, p0.lastLocation?.speed!!)
            }

        }
        val locationRequest = LocationRequest.Builder(500L)
            .build()
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    override fun onLocationChanged(location: Location) {
//        Log.d("location changed", location.latitude.toString() + " " + location.longitude.toString())
////        updateLocationIndex()
//        Log.d("location changed place holder", StationDataContent.ITEMS.toString())
//        StationDataContent.checkStation(location.latitude, location.longitude)
//        updateGmapPolyline(location)
//        SocketIo.sendLocation(location.latitude,location.longitude, location.speed)

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



    fun updateGmapPolyline(location: LatLng, speed: Float){
//        gmap.clear()
        Log.d("gps", nowLocation.toString())
        nowLocation = LatLng(location.latitude, location.longitude)

        currentLocationMarker.remove()
        currentLocationMarker = gmap.addMarker(
            MarkerOptions()
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .icon(BitmapFromVector(mContext, R.drawable.bus_icon))
                .position(nowLocation)
                .title((speed * 3.5).toString() + "km/h")
        )!!
        currentLocationMarker.showInfoWindow()
        var list:MutableList<LatLng> = arrayListOf()
        var tmp:LatLng = LatLng(0.0,0.0)
        BusStartMainScreen.gmap?.animateCamera(CameraUpdateFactory.newLatLng(nowLocation))
        StationDataContent.ITEMS.forEach {
            tmp = LatLng(it.station_latitude, it.station_longitude)
            BusStartMainScreen.gmap?.addMarker(
                MarkerOptions()
                    .position(tmp)
                    .title(it.station_name)
            )
        }
//        list.add(nowLocation)
//        for (i in nowLocationIndex until locList.size) {
//            list.add(locList[i])
//        }
//        gmap.addPolyline(
//            PolylineOptions()
//                .addAll(list)
//                .width(10f)
//                .color(Color.RED)
//        )
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
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    companion object {



    }
}