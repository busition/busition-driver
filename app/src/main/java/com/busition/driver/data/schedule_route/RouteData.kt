package com.busition.driver.data.schedule_route

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class StationListData(

    @SerializedName("station_count")
    @Expose
    var station_count: Int,

    @SerializedName("station_data")
    @Expose
    var station_data: List<StationData>,

    @SerializedName("total_passenger_count")
    @Expose
    var total_passenger_count: Int,
)
