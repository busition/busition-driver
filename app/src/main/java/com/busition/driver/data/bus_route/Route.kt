package com.busition.driver.data.bus_route

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Route(
    @SerializedName("traoptimal")
    @Expose
    val traoptimal: List<Traoptimal>
)
data class Traoptimal(
    @SerializedName("path")
    @Expose
    val path: ArrayList<ArrayList<Double>>,
)
