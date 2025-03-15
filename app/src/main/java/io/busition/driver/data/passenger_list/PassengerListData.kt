package io.busition.driver.data.passenger_list

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PassengerListData(
    @SerializedName("route_id")
    @Expose
    val route_uuid:String,

    @SerializedName("station_id")
    @Expose
    val station_uuid:String,

    @SerializedName("passenger_count")
    @Expose
    val passenger_count:Int,

    @SerializedName("passengers")
    @Expose
    val passengerList: List<PassengerData>


)
