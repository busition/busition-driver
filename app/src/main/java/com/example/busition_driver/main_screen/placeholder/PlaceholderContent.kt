package com.example.busition_driver.main_screen.placeholder

import com.example.busition_driver.data.DepartureTime
import com.example.busition_driver.main_screen.MainScreen
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