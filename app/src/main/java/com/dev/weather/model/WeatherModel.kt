package com.dev.weather.model

data class WeatherModel(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<WeatherList>,
    val message: Int
)