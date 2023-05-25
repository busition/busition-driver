package com.busition.driver.data.passenger_list

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PassengerListData(
    @SerializedName("route_uuid")
    @Expose
    val route_uuid:String,

    @SerializedName("station_uuid")
    @Expose
    val station_uuid:String,

    @SerializedName("passenger_count")
    @Expose
    val passenger_count:Int,

    @SerializedName("passenger_list")
    @Expose
    val passengerList: List<PassengerData>


)
