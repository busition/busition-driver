package com.example.busition_driver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.busition_driver.databinding.FragmentItemDetailScreenBinding
import com.example.busition_driver.databinding.FragmentStationItemScreenBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class StationDetailFragment: Fragment() {
    lateinit var binding: FragmentStationItemScreenBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStationItemScreenBinding.bind(view)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        val listFragment = childFragmentManager.findFragmentById(R.id.stationList)
        listFragment?.onCreate(savedInstanceState)
        mapFragment?.getMapAsync(callback)


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

        return inflater.inflate(R.layout.fragment_station_item_screen, container, false)
    }
}