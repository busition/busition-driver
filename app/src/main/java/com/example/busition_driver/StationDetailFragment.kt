package com.example.busition_driver

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.busition_driver.databinding.FragmentStationItemScreenBinding
import com.example.busition_driver.main_screen.MainScreen
import com.example.busition_driver.placeholder.PlaceholderContent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.properties.Delegates

class StationDetailFragment: Fragment() {
    lateinit var binding: FragmentStationItemScreenBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

//        binding.test.setOnClickListener {
//            activity?.startService(Intent(activity, FloatingService::class.java))
//            Log.d("error","error")
//            Log.d("error",activity.toString())
//        }

    }
    private val callback = OnMapReadyCallback { googleMap ->
        val seoul = LatLng(37.5666805,  126.9784147)
        googleMap.addMarker(MarkerOptions().position(seoul).title("Marker in Seoul"))
        val cameraPosition = CameraPosition.Builder()
            .target(seoul)
            .zoom(15f)
            .build()
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_station_item_screen, container, false)
        //recycler view adapter set up
        binding = FragmentStationItemScreenBinding.bind(view)
        routeId = arguments?.getInt("route_id")!!

        binding.stationItemList.stationListView.layoutManager = LinearLayoutManager(context)
        binding.stationItemList.stationListView.adapter = StationItemRecyclerAdapter(PlaceholderContent.ITEMS,activity as MainScreen)
        return view
    }

    companion object {
        var routeId by Delegates.notNull<Int>()
    }
}