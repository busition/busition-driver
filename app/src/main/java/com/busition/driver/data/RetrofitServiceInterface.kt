package com.busition.driver.data

import com.busition.driver.data.bus_route.RouteData
import com.busition.driver.data.passenger_list.PassengerListData
import com.busition.driver.data.schedule_all.ScheduleDataList
import com.busition.driver.data.schedule_route.StationListData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitServiceInterface {

    @GET("schedule/driver")
    fun getSchedule(@Header("Authorization")token:String): Call<ScheduleDataList>

    @GET("station")
    fun getStation(
        @Query("route_id") route_id: Int,
        @Query("shuttle_direction") shuttle_direction: Int
    ): Call<StationListData>

    @GET("passenger/list")
    fun getPassengerList(
        @Query("route_id") route_id: Int,
        @Query("station_id") station_id: Int,
        @Query("shuttle_direction") shuttle_direction: Int
    ): Call<PassengerListData>

    @GET("route/{route_id}")
    fun getRoute(@Path("route_id") route_id: Int,
                 @Query("shuttle_direction") shuttle_direction: Int ): Call<RouteData>
}