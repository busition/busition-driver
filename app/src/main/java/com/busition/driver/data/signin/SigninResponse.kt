package com.busition.driver.data.signin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SigninResponse(
    @Expose
    @SerializedName("access_token")
    var access_token: String,

    @Expose
    @SerializedName("token_type")
    var token_type: String,


)
