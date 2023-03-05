package com.example.busition_driver.main_screen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.example.busition_driver.Gps.LocationTraker
import com.example.busition_driver.R
import com.example.busition_driver.databinding.FragmentItemDetailScreenBinding
import com.example.busition_driver.main_screen.placeholder.PlaceholderContent
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment


class fragment_item_detial_screen : Fragment() {

    private lateinit var binding: FragmentItemDetailScreenBinding
    private lateinit var itemId: PlaceholderContent.PlaceholderItem
    private lateinit var gmap : GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arg = requireArguments()
        itemId = arg.getSerializable("item") as PlaceholderContent.PlaceholderItem


        Log.d("item", itemId.toString())


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentItemDetailScreenBinding.bind(view)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)


        binding.runBtn.setOnClickListener {
            val bundle = bundleOf("item" to binding.estimatedTime.text)
            val navController = Navigation.findNavController(view)
            navController.navigate(R.id.action_fragment_item_detial_screen_to_fragment_station_item_screen,bundle)
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


}