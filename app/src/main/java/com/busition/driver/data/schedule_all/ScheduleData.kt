package com.busition.driver.data.schedule_all

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class ScheduleData (
    @SerializedName("id")
    @Expose
    var route_uuid: Int,

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

    @SerializedName("name")
    @Expose
    var name: String,
)