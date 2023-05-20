package com.busition.driver.data.signin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SigninResponse(
    @Expose
    @SerializedName("status")
    var status: Int,

)
