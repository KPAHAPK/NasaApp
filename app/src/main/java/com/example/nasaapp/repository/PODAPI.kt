package com.example.nasaapp.repository

import com.example.nasaapp.viewmodel.PODServerResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PODAPI {
    @GET("planetary/apod")
    fun getPOD(@Query("api_key") apiKey: String): Call<PODServerResponseData>
}