package io.busition.driver.data

import io.busition.driver.data.bus_route.RouteData
import io.busition.driver.data.passenger_list.PassengerListData
import io.busition.driver.data.schedule_all.ScheduleDataList
import io.busition.driver.data.schedule_route.StationListData
import io.busition.driver.data.signin.SignIn
import io.busition.driver.data.signin.SigninResponse
import io.busition.driver.data.signup.SignUpBody
import io.busition.driver.data.signup.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitServiceInterface {

    @GET("schedule/driver/")
    fun getSchedule(@Header("Authorization") token: String): Call<ScheduleDataList>

    @GET("station")
    fun getStation(
        @Header("Authorization") token: String,
        @Query("route_id") route_uuid: Int,
    ): Call<StationListData>

    @GET("passenger/list")
    fun getPassengerList(
        @Header("Authorization") token: String,
        @Query("route_id") route_uuid: Int,
        @Query("station_id") station_uuid: Int,
    ): Call<PassengerListData>

    @GET("route/{route_uuid}")
    fun getRoute(
        @Header("Authorization") token: String,
        @Path("route_uuid") route_uuid: Int,
    ): Call<RouteData>

    @POST("users/")
    fun signUp(
        @Body body: SignUpBody
    ): Call<SignUpResponse>

    @FormUrlEncoded
    @POST("users/token")
    fun sighIn(
        @Field("username") username: String,
        @Field("password") password: String,
        @Query("device_token") device_token: String,
        @Query("busition_platform") busition_platform: String = "Busition-Driver",
    ): Call<SigninResponse>

}