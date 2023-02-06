package com.example.busition_driver

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.busition_driver.databinding.FragmentItemDetailScreenBinding


class fragment_item_detial_screen : Fragment() {

    private lateinit var binding: FragmentItemDetailScreenBinding
    private lateinit var itemId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arg = requireArguments()
        itemId = arg.getString("item").toString()
        Log.d("item",itemId)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentItemDetailScreenBinding.bind(view)
        binding.itemId.text = itemId
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemDetailScreenBinding.inflate(inflater, container, false)
        return inflater.inflate(R.layout.fragment_item_detail_screen, container, false)
    }


}