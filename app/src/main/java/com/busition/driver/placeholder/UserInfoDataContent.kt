package com.busition.driver.placeholder

import com.busition.driver.data.passenger_list.PassengerData
import java.util.ArrayList

object UserInfoDataContent {
    val ITEMS: MutableList<PassengerData> = ArrayList()

    fun makeItem(tmp:MutableList<PassengerData>){
        ITEMS.clear()
        for(i in 0 until (tmp.size)){
            addItem(tmp[i])
        }
    }


    private fun addItem(item: PassengerData) {
        ITEMS.add(item)
    }


}