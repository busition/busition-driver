package com.busition.driver.data.schedule_route

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class StationData(
    @SerializedName("station_id")
    @Expose
    var station_id: Int,

    @SerializedName("station_name")
    @Expose
    var station_name: String,

    @SerializedName("stopping_order")
    @Expose
    var stopping_order: Int,

    @SerializedName("station_latitude")
    @Expose
    var station_latitude: Double,

    @SerializedName("station_longitude")
    @Expose
    var station_longitude: Double,

    @SerializedName("passenger_count")
    @Expose
    var passenger_count: Int,

    var nowPassengerNumber:Int =0,

)
