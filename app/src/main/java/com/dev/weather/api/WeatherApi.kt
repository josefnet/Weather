package com.dev.weather.api

import com.dev.weather.model.WeatherModel
import com.dev.weather.util.Constants.API_KEY
import retrofit2.Call

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/forecast")
    fun getData(
        @Query("q")
        cityName: String = "paris",
        @Query("units")
        unitName: String = "metric",
        @Query("lang")
        language: String = "en",
        @Query("appid")
        apiKey: String = API_KEY
    ): Call<WeatherModel>
}