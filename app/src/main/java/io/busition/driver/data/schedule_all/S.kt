package io.busition.driver.data.schedule_all

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat

data class S(
    @SerializedName("S")
    @Expose
    var S: String,
){
    fun converIso(time: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val date = sdf.parse(time)
        val sdf2 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        return sdf2.format(date)
    }
}
