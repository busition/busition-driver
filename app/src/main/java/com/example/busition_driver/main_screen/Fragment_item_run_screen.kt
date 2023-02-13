package com.example.busition_driver.main_screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.busition_driver.databinding.FragmentItemRunScreenBinding
import com.google.android.gms.maps.GoogleMap

class fragment_item_run_screen : Fragment() {
    private lateinit var binding: FragmentItemRunScreenBinding
    private lateinit var googleMap: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentItemRunScreenBinding.bind(view)


    }
}

