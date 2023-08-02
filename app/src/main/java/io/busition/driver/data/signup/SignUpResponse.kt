package io.busition.driver.data.signup

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("status_code")
    @Expose
    var status_code: Int,

    @SerializedName("service_name")
    @Expose
    var service_name: String,
)
