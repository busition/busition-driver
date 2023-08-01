package com.busition.driver.placeholder

import android.location.Location
import android.util.Log
import androidx.fragment.app.Fragment
import com.busition.driver.StationItemRecyclerAdapter
import com.busition.driver.data.schedule_route.StationData
import com.busition.driver.main_screen.FragmentItemDetailScreen
import com.busition.driver.main_screen.MainScreen
import java.lang.Math.asin
import java.lang.Math.atan2
import java.lang.Math.cos
import java.lang.Math.sin
import java.lang.Math.sqrt
import java.util.ArrayList
import kotlin.math.pow


object PlaceholderContent {


    val ITEMS: MutableList<StationData> = ArrayList()
    var ind:Int = 0
    var check: Boolean = false

    init {

    }
    fun makeItem(tmp:MutableList<StationData>){
        ITEMS.clear()
        if (tmp.size == 0){
            addItem(StationData(-1,"",0,0.0,0.0,0,"",0,true))
        }
        for(i in 0 until (tmp.size)){
            addItem(tmp.get(i))
        }
    }
    private fun addItem(item: StationData) {
        ITEMS.add(item)

    }

    fun checkStation(lat:Double, lon:Double){
        val tmpDistance = getDistance(ITEMS.get(ind).station_latitude, ITEMS.get(ind).station_longitude, lat, lon)
        if (tmpDistance < 300 && !check){
            check = true
            MainScreen.scanning = true
            MainScreen.stop = true
            Log.d("change station", "get in range")
            ITEMS.get(ind).nowStation = true
            FragmentItemDetailScreen.stationItemRecyclerAdapter.notifyDataSetChanged()

        }
        if ( 300 < tmpDistance && check){
            check = false
            MainScreen.stop = false
            Log.d("change station", "get out range")
            ITEMS.get(ind).station_state = "arrived"
            FragmentItemDetailScreen.stationItemRecyclerAdapter.notifyDataSetChanged()
        }
        if (tmpDistance > 500 && !check){
            check = false
            ITEMS.get(ind).nowStation = false
            if(ind+1 == ITEMS.size){
                ITEMS.get(ind).station_state = "done"
                FragmentItemDetailScreen.stationItemRecyclerAdapter.notifyDataSetChanged()
            }else{
                ind++
                ITEMS.get(ind).nowStation = true
                Log.d("change station", ITEMS.get(ind).station_name)
                MainScreen.scanning = false
                FragmentItemDetailScreen.stationItemRecyclerAdapter.notifyDataSetChanged()
            }

        }
        Log.d("distance" , tmpDistance.toString())
    }
    fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val tmp = Location("A")
        tmp.latitude = lat1
        tmp.longitude = lon1
        val tmp2 = Location("B")
        tmp2.latitude = lat2
        tmp2.longitude = lon2

        return tmp.distanceTo(tmp2)
    }




}


