package com.busition.driver.data.schedule_all

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ScheduleDataList(
    @SerializedName("schedule_count")
    @Expose
    var schedule_count: Int,

    @SerializedName("schedule_data")
    @Expose
    var schedule_data: List<ScheduleData>,

)
