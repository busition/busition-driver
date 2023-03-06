package com.example.busition_driver.data.passenger_list

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PassengerData(
    @SerializedName("passenger_id")
    @Expose
    val passenger_id:String,

    @SerializedName("passenger_name")
    @Expose
    val passenger_name:String,

    @SerializedName("passenger_phone")
    @Expose
    val passenger_phone:String,

    @SerializedName("passenger_email")
    @Expose
    val passenger_email:String
)