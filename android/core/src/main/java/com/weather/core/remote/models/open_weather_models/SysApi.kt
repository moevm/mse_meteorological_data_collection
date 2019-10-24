package com.weather.core.remote.models.open_weather_models

data class SysApi(
    val country: String,
    val id: Int,
    val message: Double,
    val sunrise: Int,
    val sunset: Int,
    val type: Int
)