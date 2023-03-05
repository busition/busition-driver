package com.example.busition_driver.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitServiceInterface {

    @GET("schedule/all")
    fun getSchedule(@Query("driver_id") driver_id: Int): Call<ScheduleDataList>
}