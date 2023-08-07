package io.busition.driver.main_screen.dataholder

import android.util.Log
import io.busition.driver.data.passenger_list.PassengerData
import java.util.ArrayList

object UserInfoDataContent {
    val ITEMS: MutableList<PassengerData> = ArrayList()

    fun makeItem(tmp:MutableList<PassengerData>){
        ITEMS.clear()
        for(i in 0 until (tmp.size)){
            addItem(tmp[i])
        }
    }


    fun addItem(item: PassengerData) {
        ITEMS.add(item)
        Log.d("add item", item.toString())
    }


}