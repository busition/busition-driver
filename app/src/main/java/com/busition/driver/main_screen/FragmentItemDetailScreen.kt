package com.busition.driver.main_screen

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.busition.driver.*
import com.busition.driver.data.RetrofitServiceInterface
import com.busition.driver.data.bus_route.RouteData
import com.busition.driver.data.schedule_route.StationData
import com.busition.driver.data.schedule_route.StationListData
import com.busition.driver.databinding.FragmentItemDetailScreenBinding
import com.busition.driver.placeholder.PlaceholderContent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FragmentItemDetailScreen : Fragment() {

    private lateinit var binding: FragmentItemDetailScreenBinding

    private lateinit var gmap: GoogleMap

    private lateinit var station_count: TextView
    private lateinit var estimated_time: TextView
    private lateinit var passenger_count: TextView
    private lateinit var travel_distance: TextView
    private lateinit var stationItemRecyclerAdapter: StationItemRecyclerAdapter

    private lateinit var secret: String

    private var route_list = ArrayList<ArrayList<Double>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arg = requireArguments()
        route_id = arg.getInt("route_id")
        shuttle_direction = arg.getInt("shuttle_direction")
        //imsi use key

        Log.d("route_id", route_id.toString())


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        station_count = binding.numberOfStation
        estimated_time = binding.estimatedTime
        passenger_count = binding.numberOfPasenger
        travel_distance = binding.travelDistance
        requestRouteApiServer()
        requestApiServer()

        binding.runBtn.setOnClickListener {
            val bundle = bundleOf("item" to binding.estimatedTime.text)
            val navController = Navigation.findNavController(view)
//            navController.navigate(
//                R.id.action_fragment_item_detial_screen_to_fragment_station_item_screen,
//                bundle
//            )
            binding.textList.gone()
            binding.itemListLay.show()
            activity?.startService(Intent(activity,GpsForegroundService::class.java))
            Toast.makeText(activity,"service start", Toast.LENGTH_LONG).show()
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
        val view = inflater.inflate(R.layout.fragment_item_detail_screen,container,false)
        binding = FragmentItemDetailScreenBinding.bind(view)
        binding.itemList.stationListView.layoutManager = LinearLayoutManager(context)
        stationItemRecyclerAdapter = StationItemRecyclerAdapter(PlaceholderContent.ITEMS,activity as MainScreen)
        binding.itemList.stationListView.adapter = stationItemRecyclerAdapter


        return view
    }

    fun requestRouteApiServer(){
        val retrofit = Retrofit.Builder()
            .baseUrl(base_api_url)
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
                    Log.d("api", route.toString())
                    Log.d("api", "onResponse: ${response.body()?.route?.traoptimal?.get(0)?.path}")

                    route_list = route?.route?.traoptimal?.get(0)?.path as ArrayList<ArrayList<Double>>
                    makePolyRoute()
                } else {
                    Log.d("api", "onResponse: $response")
                }
            }

            override fun onFailure(call: retrofit2.Call<RouteData>, t: Throwable) {
                Log.d("api", "onFailure: ${t.message}")
            }
        })
    }
    fun requestApiServer() {
        binding.textList.gone()
        binding.loadBar.show()
        val retrofit = Retrofit.Builder()
            .baseUrl(base_api_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RetrofitServiceInterface::class.java)

        service.getStation(route_id, shuttle_direction).enqueue(object : retrofit2.Callback<StationListData> {
            override fun onResponse(
                call: retrofit2.Call<StationListData>,
                response: retrofit2.Response<StationListData>
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
                    Log.d("response", "onResponse: $response")
                }
                binding.loadBar.gone()
                binding.textList.show()
                makeRoute()

            }

            override fun onFailure(call: retrofit2.Call<StationListData>, t: Throwable) {
                Log.d("route", "onFailure: ${t.message}")
            }
        })

    }

    fun makeRoute() {
        gmap.clear()
        var list: MutableList<LatLng> = mutableListOf()
        var tmp:LatLng = LatLng(0.0,0.0)
        var boundsBuilder = LatLngBounds.Builder()
        PlaceholderContent.ITEMS.forEach {
            tmp = LatLng(it.station_latitude, it.station_longitude)
            list.add(tmp)
            Log.d("route", it.station_name)
            gmap.addMarker(
                MarkerOptions()
                    .position(tmp)
                    .title(it.station_name)
            )
            boundsBuilder.include(tmp)
        }
        var bounds = boundsBuilder.build()
        gmap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))

    }

    fun makePolyRoute(){
        var list: MutableList<LatLng> = mutableListOf()
        var tmp:LatLng = LatLng(0.0,0.0)
        list.clear()
        for (i in 0 until route_list.size) {
            tmp = LatLng(route_list[i][1], route_list[i][0])
            list.add(tmp)


        }
        gmap.addPolyline(
            PolylineOptions()
                .addAll(list)
                .width(10f)
                .color(Color.RED)
        )
    }



    //in use global service google route api decode code
//    fun getRouteUrl() {
//        var origin: String = ""
//        var dest: String = ""
//        var waypoints: String = "&waypoints="
//
//        val url = "https://maps.googleapis.com/maps/api/directions/json?origin=Disneyland&destination=Universal+Studios+Hollywood"+
//                "&key=AIzaSyBUNnlsuMCJth1VWvY4HV0pa5eoFmqya_A"
//        Log.d("route", url)
//        GetDirection(url).execute()
//    }




//    @SuppressLint("StaticFieldLeak")
//    private inner class GetDirection(val url: String) :
//        AsyncTask<Void, Void, List<List<LatLng>>>() {
//        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
//            val client = OkHttpClient()
//            val request = Request.Builder().url(url).build()
//            val response = client.newCall(request).execute()
//            val data = response.body()?.string()
//            Log.d("route", data.toString())
//            val result = ArrayList<List<LatLng>>()
//            try {
//                val respObj = Gson().fromJson(data, MapData::class.java)
//                val path = ArrayList<LatLng>()
//                for (i in 0 until respObj.routes[0].legs[0].steps.size) {
//                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
//                }
//                result.add(path)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//            return result
//        }
//
//        override fun onPostExecute(result: List<List<LatLng>>) {
//            val lineoption = PolylineOptions()
//            for (i in result.indices) {
//                lineoption.addAll(result[i])
//                lineoption.width(10f)
//                lineoption.color(Color.GREEN)
//                lineoption.geodesic(true)
//            }
//            gmap.addPolyline(lineoption)
//        }
//    }


//    fun decodePolyline(encoded: String): List<LatLng> {
//        val poly = ArrayList<LatLng>()
//        var index = 0
//        val len = encoded.length
//        var lat = 0
//        var lng = 0
//        while (index < len) {
//            var b: Int
//            var shift = 0
//            var result = 0
//            do {
//                b = encoded[index++].code - 63
//                result = result or (b and 0x1f shl shift)
//                shift += 5
//            } while (b >= 0x20)
//            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
//            lat += dlat
//            shift = 0
//            result = 0
//            do {
//                b = encoded[index++].code - 63
//                result = result or (b and 0x1f shl shift)
//                shift += 5
//            } while (b >= 0x20)
//            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
//            lng += dlng
//            val latLng = LatLng((lat.toDouble() / 1E5), (lng.toDouble() / 1E5))
//            poly.add(latLng)
//        }
//        Log.d("poly", poly.toString())
//        return poly
//
//    }

    companion object {
        val stationDataList: MutableList<StationData> = ArrayList()
        var route_id: Int = 0
        var shuttle_direction: Int = 0
    }
}