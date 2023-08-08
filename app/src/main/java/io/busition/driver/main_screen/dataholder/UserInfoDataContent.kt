package io.busition.driver.main_screen.dataholder

import android.util.Log
import io.busition.driver.data.passenger_list.PassengerData
import java.util.ArrayList

object UserInfoDataContent {
    val ITEMS: MutableList<PassengerData> = ArrayList()
    var number = 0
    var onNumber = 0

    fun makeItem(tmp:MutableList<PassengerData>){
        ITEMS.clear()
        for(i in 0 until (tmp.size)){
            addItem(tmp[i])
        }
        if (ITEMS.size == 0){
            addItem(PassengerData("","","","11111111111111111111111111111111"))
        }
    }


    fun addItem(item: PassengerData) {
        ITEMS.add(item)
        Log.d("add item", item.toString())
    }


}