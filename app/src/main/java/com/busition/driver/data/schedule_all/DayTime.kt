package com.busition.driver.data.schedule_all

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class DayTime(
    @SerializedName("day_of_week")
    @Expose
    var day_of_week: String,

    @SerializedName("time")
    @Expose
    var time: String,

){
    fun converIso(tmp:String): String {
        val t =  tmp
        val format = SimpleDateFormat("'T'HH:mm:ssX", Locale.KOREAN)
        val date = format.parse(t)
        val formatter = SimpleDateFormat("a HH - mm - ss", Locale.KOREAN)
        val dateString: String = formatter.format(date!!)
        return dateString
    }
}
