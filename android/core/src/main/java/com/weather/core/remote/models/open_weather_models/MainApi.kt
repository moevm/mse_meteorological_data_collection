package com.weather.core.remote.models.open_weather_models

data class MainApi(
    val humidity: Int,
    val pressure: Double,
    val temp: Double,
    val temp_max: Double,
    val temp_min: Double
)