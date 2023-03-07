package com.busition.driver.data

import com.busition.driver.data.passenger_list.PassengerListData
import com.busition.driver.data.schedule_all.ScheduleDataList
import com.busition.driver.data.schedule_route.RouteData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitServiceInterface {

    @GET("schedule/all")
    fun getSchedule(@Query("driver_id") driver_id: Int): Call<ScheduleDataList>

    @GET("route/{route_id}")
    fun getRoute(@Path("route_id") route_id: Int): Call<RouteData>

    @GET("passenger/list")
    fun getPassengerList(
        @Query("route_id") route_id: Int,
        @Query("station_id") station_id: Int
    ): Call<PassengerListData>
}