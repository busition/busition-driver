package io.busition.driver.data.schedule_all

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DepartureTime(
    @SerializedName("day_time")
    @Expose
    var day_time: DayTime,


    ){
    fun getDayTime(): DayTime {
        return day_time
    }

    fun setDayTime(day_time: DayTime) {
        this.day_time = day_time
    }
}
