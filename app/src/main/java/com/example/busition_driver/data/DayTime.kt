package com.example.busition_driver.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DayTime(
    @SerializedName("day_of_week")
    @Expose
    var day_of_week: String,

    @SerializedName("time")
    @Expose
    var time: String,

)
