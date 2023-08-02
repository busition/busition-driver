package com.busition.driver.main_screen.dataholder

import java.util.ArrayList

object ScheduleData {

    val ScheduleItems: MutableList<PlaceholderItem> = ArrayList()

    fun addItem(item: PlaceholderItem) {
        ScheduleItems.add(item)
    }


    data class PlaceholderItem(
        val route_uuid: Int,
        val departure_point: String,
        val destination: String,
//        val departure_time: DepartureTime,
        val route_name:String,
        val shuttle_direction: Int,
    )
}