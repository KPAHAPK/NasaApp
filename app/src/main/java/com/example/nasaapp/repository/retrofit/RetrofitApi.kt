package com.example.nasaapp.repository.retrofit

import com.example.nasaapp.repository.responsedata.PODServerResponseData
import com.example.nasaapp.repository.responsedata.SolarFlareServerResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApi {
    @GET("planetary/apod")
    fun getPOD(
        @Query("api_key") apiKey: String,
        @Query("date") date: String
    ): Call<PODServerResponseData>

    @GET("DONKI/FLR")
    fun getSolarFlare(
        @Query("api_key") apiKey: String,
        @Query("startDate") startDate: String
    ): Call<List<SolarFlareServerResponseData>>

}