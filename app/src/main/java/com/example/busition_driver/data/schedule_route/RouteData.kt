package com.example.busition_driver.data.schedule_route

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RouteData(
    @SerializedName("station_count")
    @Expose
    var station_count: Int,

    @SerializedName("station_data")
    @Expose
    var station_data: List<StationData>,
)
