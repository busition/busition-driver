package io.busition.driver.main_screen.dataholder

import android.location.Location
import android.util.Log
import io.busition.driver.data.schedule_route.StationData
import io.busition.driver.main_screen.fragment.BusStartMainScreen
import io.busition.driver.main_screen.MainScreen
import java.util.ArrayList


object StationDataContent {


    val ITEMS: MutableList<StationData> = ArrayList()
    var ind:Int = 0
    var inStationRange: Boolean = false
    var check:Boolean = false

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
        ind = 0
    }
    private fun addItem(item: StationData) {
        ITEMS.add(item)

    }

    fun checkStation(lat:Double, lon:Double){
        val tmpDistance = getDistance(ITEMS.get(ind).station_latitude, ITEMS.get(ind).station_longitude, lat, lon)
        if (tmpDistance < 300 && !inStationRange){
            inStationRange = true
            MainScreen.scanning = true
            MainScreen.stop = true
            Log.d("change station", "get in range")
            ITEMS.get(ind).station_state = "arrived"
            ITEMS.get(ind).nowStation = true
            BusStartMainScreen.stationItemRecyclerAdapter.notifyDataSetChanged()

        }
        if ( 300 < tmpDistance && inStationRange){
            inStationRange = false
            check = true
            MainScreen.stop = false
            Log.d("change station", "get out range")
            ITEMS.get(ind).station_state = "done"
            BusStartMainScreen.stationItemRecyclerAdapter.notifyDataSetChanged()
        }
        if (tmpDistance > 500 && check){
            check = false
            ITEMS.get(ind).nowStation = false
            if(ind +1 == ITEMS.size){
                ITEMS.get(ind).station_state = "done"
                BusStartMainScreen.stationItemRecyclerAdapter.notifyDataSetChanged()
            }else{
                ind++
                ITEMS.get(ind).nowStation = true
                Log.d("change station", ITEMS.get(ind).station_name)
                MainScreen.scanning = false
                BusStartMainScreen.stationItemRecyclerAdapter.notifyDataSetChanged()
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


