package com.busition.driver.main_screen.placeholder

import com.busition.driver.data.schedule_all.DepartureTime
import com.busition.driver.main_screen.MainScreen
import java.io.Serializable
import java.util.ArrayList
import java.util.HashMap

object PlaceholderContent {

    val ITEMS: MutableList<PlaceholderItem> = ArrayList()
    val ITEM_MAP: MutableMap<String, PlaceholderItem> = HashMap()
    val driver_id = MainScreen.driver_id
    private val COUNT = 5

    init {

    }

    fun addItem(item: PlaceholderItem) {
        val timeData = item.departure_time.day_time.converIso(item.departure_time.day_time.time)
        item.departure_time.day_time.time = timeData
        ITEMS.add(item)

    }


    data class PlaceholderItem(
        val route_id: Int,
        val departure_point: String,
        val destination: String,
        val departure_time: DepartureTime,
        val shuttle_direction: Int,
    ) : Serializable {

    }


}