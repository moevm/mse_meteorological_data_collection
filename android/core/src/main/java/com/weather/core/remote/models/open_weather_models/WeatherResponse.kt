package com.weather.core.remote.models.open_weather_models

data class WeatherResponse(
    val base: String,
    val clouds: CloudsApi,
    val cod: Int,
    val coord: CoordApi,
    val dt: Int,
    val id: Int,
    val main: MainApi,
    val name: String,
    val sys: SysApi,
    val timezone: Int,
    val weather: List<WeatherApi>,
    val wind: WindApi
)