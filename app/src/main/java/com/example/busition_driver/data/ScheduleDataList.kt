package com.example.busition_driver.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ScheduleDataList(
    @SerializedName("schedule_count")
    @Expose
    var schedule_count: Int,

    @SerializedName("schedule_data")
    @Expose
    var schedule_data: List<ScheduleData>,
){
    fun getScheduleCount(): Int {
        return schedule_count
    }

    fun setScheduleCount(schedule_count: Int) {
        this.schedule_count = schedule_count
    }

    fun getScheduleData(): List<ScheduleData> {
        return schedule_data
    }

    fun setScheduleData(schedule_data: List<ScheduleData>) {
        this.schedule_data = schedule_data
    }
}
