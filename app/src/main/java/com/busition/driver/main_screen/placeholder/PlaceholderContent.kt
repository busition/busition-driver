package com.busition.driver.main_screen.placeholder

import android.util.Log
import com.busition.driver.data.schedule_all.DepartureTime
import com.busition.driver.data.schedule_all.S
import com.busition.driver.main_screen.MainScreen
import java.io.Serializable
import java.util.ArrayList
import java.util.HashMap

object PlaceholderContent {

    val ITEMS: MutableList<PlaceholderItem> = ArrayList()
    val ITEM_MAP: MutableMap<String, PlaceholderItem> = HashMap()
    val driver_uuid = MainScreen.driver_uuid
    private val COUNT = 5

    init {

    }

    fun addItem(item: PlaceholderItem) {

        Log.d("PlaceholderContent", "addItem: ${item.departure_time.day_time}")
        if (item.departure_time.day_time.time == null || item.departure_time.day_time.time.toString() == "null") {
            Log.d("PlaceholderContent", "addItem: null")
        }else{
            val timeData = item.departure_time.day_time.converIso(item.departure_time.day_time.time.S)
            item.departure_time.day_time.time.S = timeData
            ITEMS.add(item)
        }


    }


    data class PlaceholderItem(
        val route_uuid: String,
        val departure_point: String,
        val destination: String,
        val departure_time: DepartureTime,
        val shuttle_direction: Int,
    ) : Serializable {

    }


}