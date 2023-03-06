package com.example.busition_driver.placeholder

import com.example.busition_driver.StationDetailFragment
import com.example.busition_driver.data.schedule_route.StationData
import java.util.ArrayList
import java.util.HashMap


object PlaceholderContent {


    val ITEMS: MutableList<StationData> = ArrayList()


    init {

    }
    fun makeItem(tmp:MutableList<StationData>){
        ITEMS.clear()
        for(i in 0 until (tmp.size)){
            addItem(tmp.get(i))
        }
    }
    private fun addItem(item: StationData) {
        ITEMS.add(item)

    }



}