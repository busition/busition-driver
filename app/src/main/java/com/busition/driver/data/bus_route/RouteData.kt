package com.busition.driver.data.bus_route

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class RouteData(

    @SerializedName("path_count")
    @Expose
    val code: Int,

    @SerializedName("message")
    @Expose
    val message: String,

    @SerializedName("currentDateTime")
    @Expose
    val currentDateTime: String,

    @SerializedName("path")
    @Expose
    val path: ArrayList<ArrayList<Double>>,
)

fun converIso(tmp:String): String {
    val t =  tmp
    val format = SimpleDateFormat("'T'HH:mm:ssX", Locale.KOREAN)
    val date = format.parse(t)
    val formatter = SimpleDateFormat("a HH - mm - ss", Locale.KOREAN)
    val dateString: String = formatter.format(date!!)
    return dateString
}
