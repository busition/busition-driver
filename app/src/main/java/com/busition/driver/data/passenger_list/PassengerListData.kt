package com.busition.driver.data.passenger_list

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PassengerListData(
    @SerializedName("route_id")
    @Expose
    val route_id:Int,

    @SerializedName("station_id")
    @Expose
    val station_id:Int,

    @SerializedName("passenger_count")
    @Expose
    val passenger_count:Int,

    @SerializedName("passenger_list")
    @Expose
    val passengerList: List<PassengerData>


)
