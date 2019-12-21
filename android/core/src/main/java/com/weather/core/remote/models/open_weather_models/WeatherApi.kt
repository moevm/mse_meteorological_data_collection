package com.weather.core.remote.models.open_weather_models

data class WeatherApi(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)