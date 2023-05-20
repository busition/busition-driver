package com.busition.driver.data

import com.busition.driver.data.bus_route.RouteData
import com.busition.driver.data.passenger_list.PassengerListData
import com.busition.driver.data.schedule_all.ScheduleDataList
import com.busition.driver.data.schedule_route.StationListData
import com.busition.driver.data.signup.SignUpBody
import com.busition.driver.data.signup.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitServiceInterface {

    @GET("schedule/driver")
    fun getSchedule(@Header("Authorization") token: String): Call<ScheduleDataList>

    @GET("station")
    fun getStation(
        @Header("Authorization") token: String,
        @Query("route_uuid") route_uuid: String,
        @Query("shuttle_direction") shuttle_direction: Int
    ): Call<StationListData>

    @GET("passenger/list")
    fun getPassengerList(
        @Header("Authorization") token: String,
        @Query("route_uuid") route_uuid: String,
        @Query("station_uuid") station_uuid: String,
        @Query("shuttle_direction") shuttle_direction: Int
    ): Call<PassengerListData>

    @GET("route/{route_uuid}")
    fun getRoute(
        @Header("Authorization") token: String,
        @Path("route_uuid") route_uuid: String,
        @Query("shuttle_direction") shuttle_direction: Int
    ): Call<RouteData>

    @POST("signup")
    fun signUp(
        @Header("Authorization") token: String,
        @Body body: SignUpBody
    ): Call<SignUpResponse>

}