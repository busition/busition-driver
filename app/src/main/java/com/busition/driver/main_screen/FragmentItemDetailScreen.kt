package com.busition.driver.main_screen

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.marginBottom
import androidx.navigation.Navigation
import com.busition.driver.R
import com.busition.driver.base_url
import com.busition.driver.data.RetrofitServiceInterface
import com.busition.driver.data.map.MapData
import com.busition.driver.data.schedule_route.RouteData
import com.busition.driver.data.schedule_route.StationData
import com.busition.driver.databinding.FragmentItemDetailScreenBinding
import com.busition.driver.gone
import com.busition.driver.placeholder.PlaceholderContent
import com.busition.driver.show
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FragmentItemDetailScreen : Fragment() {

    private lateinit var binding: FragmentItemDetailScreenBinding

    private lateinit var gmap: GoogleMap

    private lateinit var station_count: TextView
    private lateinit var estimated_time: TextView
    private lateinit var passenger_count: TextView
    private lateinit var travel_distance: TextView

    private lateinit var secret: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arg = requireArguments()
        route_id = arg.getInt("route_id")
        shuttle_direction = arg.getInt("shuttle_direction")
        //imsi use key
        val ai: ApplicationInfo = requireContext().packageManager.getApplicationInfo(
            requireContext().packageName,
            PackageManager.GET_META_DATA
        )
        secret = ai.metaData["com.google.android.geo.API_KEY"].toString()

        Log.d("route_id", route_id.toString())


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentItemDetailScreenBinding.bind(view)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        station_count = binding.numberOfStation
        estimated_time = binding.estimatedTime
        passenger_count = binding.numberOfPasenger
        travel_distance = binding.travelDistance
        requestApiServer()
        binding.runBtn.setOnClickListener {
            val bundle = bundleOf("item" to binding.estimatedTime.text)
            val navController = Navigation.findNavController(view)
            navController.navigate(
                R.id.action_fragment_item_detial_screen_to_fragment_station_item_screen,
                bundle
            )
//                val loc = LocationTraker(binding.root.context, activity as MainScreen, gmap)
        }
//        val bundle = bundleOf("item" to item.destinationData)
//        val navController = Navigation.findNavController(holder.itemView)
//        navController.navigate(R.id.action_scheduleList_to_fragment_item_detial_screen,bundle)

    }


    private val callback = OnMapReadyCallback { googleMap ->
        gmap = googleMap
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_item_detail_screen, container, false)
    }

    fun requestApiServer() {
        binding.textList.gone()
        binding.loadBar.show()
        val retrofit = Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RetrofitServiceInterface::class.java)

        service.getRoute(route_id, shuttle_direction).enqueue(object : retrofit2.Callback<RouteData> {
            override fun onResponse(
                call: retrofit2.Call<RouteData>,
                response: retrofit2.Response<RouteData>
            ) {
                if (response.isSuccessful) {
                    val route = response.body()
                    Log.d("route", route.toString())
                    var tt: String = station_count.text as String
                    station_count.text =
                        appendString(station_count.text.toString(), route?.station_count.toString())
                    passenger_count.text =
                        appendString(passenger_count.text.toString(), route?.total_passenger_count.toString(), " 명")
                    for (i in 0 until (route?.station_count ?: 0)) {
                        val tmp = StationData(
                            route?.station_data?.get(i)?.station_id ?: 0,
                            route?.station_data?.get(i)?.station_name ?: "",
                            route?.station_data?.get(i)?.stopping_order ?: 0,
                            route?.station_data?.get(i)?.station_latitude ?: 0.0,
                            route?.station_data?.get(i)?.station_longitude ?: 0.0,
                            route?.station_data?.get(i)?.passenger_count ?: 0
                        )
                        stationDataList.add(tmp)
                    }
                    PlaceholderContent.makeItem(stationDataList)

                } else {
                    Log.d("route", "onResponse: $response")
                }
                binding.loadBar.gone()
                binding.textList.show()


            }

            override fun onFailure(call: retrofit2.Call<RouteData>, t: Throwable) {
                Log.d("route", "onFailure: ${t.message}")
            }
        })
    }

    fun makeRoute() {
        var list: MutableList<LatLng> = mutableListOf()
        var tmp:LatLng = LatLng(0.0,0.0)
        PlaceholderContent.ITEMS.forEach {
            tmp = LatLng(it.station_latitude, it.station_longitude)
            list.add(tmp)
            Log.d("route", it.station_name)
            gmap.addMarker(
                MarkerOptions()
                    .position(tmp)
                    .title(it.station_name)
            )
        }

        gmap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    PlaceholderContent.ITEMS[0].station_latitude,
                    PlaceholderContent.ITEMS[0].station_longitude
                ), 15f
            )
        )
    }

    fun getRouteUrl(list: List<LatLng>) {
        var origin: String = ""
        var dest: String = ""
        var waypoints: String = "&waypoints="
        for (i in 0 until list.size) {
            if (i == 0) {
                origin = "origin=${list[i].latitude},${list[i].longitude}"
            } else if (i == list.size - 1) {
                dest = "&destination=${list[i].latitude},${list[i].longitude}"
            } else {
                waypoints = appendString(waypoints, "${list[i].latitude},${list[i].longitude}|")
            }

        }
        val url = "https://maps.googleapis.com/maps/api/directions/json?" +
                origin +
                dest +
                "&sensor=false" +

                "&key=$secret"
        Log.d("route", url)
        GetDirection(url).execute()
    }

    fun appendString(a: String, b: String,c:String=""): String {
        return buildString {
            append(a)
            append(b)
            append(c)
        }
    }

    @SuppressLint("StaticFieldLeak")
    private inner class GetDirection(val url: String) :
        AsyncTask<Void, Void, List<List<LatLng>>>() {
        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body()?.string()
            Log.d("route", data.toString())
            val result = ArrayList<List<LatLng>>()
            try {
                val respObj = Gson().fromJson(data, MapData::class.java)
                val path = ArrayList<LatLng>()
                for (i in 0 until respObj.routes[0].legs[0].steps.size) {
                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                }
                result.add(path)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result
        }

        override fun onPostExecute(result: List<List<LatLng>>) {
            val lineoption = PolylineOptions()
            for (i in result.indices) {
                lineoption.addAll(result[i])
                lineoption.width(10f)
                lineoption.color(Color.GREEN)
                lineoption.geodesic(true)
            }
            gmap.addPolyline(lineoption)
        }
    }

    fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val latLng = LatLng((lat.toDouble() / 1E5), (lng.toDouble() / 1E5))
            poly.add(latLng)
        }
        return poly
    }

    companion object {
        val stationDataList: MutableList<StationData> = ArrayList()
        var route_id: Int = 0
        var shuttle_direction: Int = 0
    }
}