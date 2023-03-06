package com.example.busition_driver.main_screen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.example.busition_driver.R
import com.example.busition_driver.data.RetrofitServiceInterface
import com.example.busition_driver.data.schedule_route.RouteData
import com.example.busition_driver.data.schedule_route.StationData
import com.example.busition_driver.databinding.FragmentItemDetailScreenBinding
import com.example.busition_driver.placeholder.PlaceholderContent
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.properties.Delegates


class FragmentItemDetailScreen : Fragment() {

    private lateinit var binding: FragmentItemDetailScreenBinding

    private lateinit var gmap: GoogleMap

    private lateinit var station_count: TextView
    private lateinit var estimated_time: TextView
    private lateinit var passenger_count: TextView
    private lateinit var travel_distance: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arg = requireArguments()
        route_id = arg.getInt("route_id")


        Log.d("route_id", route_id.toString())


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentItemDetailScreenBinding.bind(view)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        station_count =  binding.numberOfStation
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
        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.busition.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RetrofitServiceInterface::class.java)

        service.getRoute(route_id).enqueue(object : retrofit2.Callback<RouteData> {
            override fun onResponse(
                call: retrofit2.Call<RouteData>,
                response: retrofit2.Response<RouteData>
            ) {
                if (response.isSuccessful) {
                    val route = response.body()
                    Log.d("route", route.toString())
                    station_count.text = route?.station_count.toString()
                    for (i in 0 until (route?.station_count ?: 0)){
                        val tmp = StationData(
                            route?.station_data?.get(i)?.station_id?:0,
                            route?.station_data?.get(i)?.station_name?:"",
                            route?.station_data?.get(i)?.stopping_order?:0,
                            route?.station_data?.get(i)?.station_latitude?:0.0,
                            route?.station_data?.get(i)?.station_longitude?:0.0
                        )
                        stationDataList.add(tmp)
                    }
                    PlaceholderContent.makeItem(stationDataList)
                }else{
                    Log.d("route", "onResponse: ${response}")
                }
            }

            override fun onFailure(call: retrofit2.Call<RouteData>, t: Throwable) {
                Log.d("route", "onFailure: ${t.message}")
            }
        })
    }


    companion object{
        val stationDataList : MutableList<StationData> = ArrayList()
        var route_id:Int = 0
    }
}