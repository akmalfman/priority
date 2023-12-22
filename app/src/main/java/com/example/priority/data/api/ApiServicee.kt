package com.example.priority.data.api

import com.example.priority.data.response.AqiResponse
import okhttp3.*
import retrofit2.http.*
import retrofit2.*


interface ApiServicee {
    @GET("nearest_city")
    suspend fun getAqi(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("key") key : String,
    ): AqiResponse
}