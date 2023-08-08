package io.busition.driver.data.passenger_list

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PassengerData(


    @SerializedName("name")
    @Expose
    val passenger_name:String,

    @SerializedName("phone")
    @Expose
    val passenger_phone:String,

    @SerializedName("email")
    @Expose
    val passenger_email:String,

    @SerializedName("service_uuid")
    @Expose
    val passenger_uid:String = "11111111111111111111111111111111",

    var station_uid:String = "",
)