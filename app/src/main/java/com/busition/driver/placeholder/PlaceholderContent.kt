package com.busition.driver.placeholder

import com.busition.driver.data.schedule_route.StationData
import java.util.ArrayList


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