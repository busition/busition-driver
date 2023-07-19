package com.busition.driver.data.schedule_route

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class StationData(
    @SerializedName("station_uuid")
    @Expose
    var station_uuid: Int,

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

    @SerializedName("travel_distance")
    @Expose
    var travel_distance: String,

    var nowPassengerNumber: Int = 0,
    var nowStation: Boolean = false

)
