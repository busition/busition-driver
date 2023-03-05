package com.example.busition_driver.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class ScheduleData (
    @SerializedName("route_id")
    @Expose
    var route_id: Int,

    @SerializedName("departure_point")
    @Expose
    var departure_point: String,

    @SerializedName("destination")
    @Expose
    var destination: String,

    @SerializedName("departure_time")
    @Expose
    var departure_time: DepartureTime,

    @SerializedName("shuttle_direction")
    @Expose
    var shuttle_direction: Int,
)