package com.busition.driver.placeholder

import com.busition.driver.data.schedule_route.StationData
import java.util.ArrayList


object PlaceholderContent {


    val ITEMS: MutableList<StationData> = ArrayList()


    init {

    }
    fun makeItem(tmp:MutableList<StationData>){
        ITEMS.clear()
        if (tmp.size == 0){
            addItem(StationData("","",0,0.0,0.0,0,0,true))
        }
        for(i in 0 until (tmp.size)){
            addItem(tmp.get(i))
        }
    }
    private fun addItem(item: StationData) {
        ITEMS.add(item)

    }



}