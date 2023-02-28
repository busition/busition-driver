package com.example.busition_driver.main_screen.placeholder

import android.util.Log
import com.example.busition_driver.main_screen.MyItemRecyclerViewAdapter
import java.util.ArrayList
import java.util.HashMap

object PlaceholderContent {

    val ITEMS: MutableList<ScheduleClass> = ArrayList()

    private val COUNT = 10

    init {
        var test = ""
        for (i in 1..COUNT) {
            test = "test $i"
            addItem(createPlaceholderItem("test", test, test, "19:00", "등교"))
        }
    }

    private fun addItem(item: ScheduleClass) {
        ITEMS.add(item)
    }

    private fun createPlaceholderItem(
        routeId: String, departurePoint: String,
        destination: String,
        departureTime: String,
        shuttleDirection: String
    ): ScheduleClass {
        return ScheduleClass(routeId, departurePoint, destination, departureTime, shuttleDirection)
    }

}