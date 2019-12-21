package com.weather.core.remote.models.open_weather_models

data class ForecastResponse(
    val cod: Long,
    val message: String,
    val cnt: Long,
    val list: List<ForecastApiItem>
)

data class ForecastApiItem(
    val dt: Long,
    val main: MainApi,
    val weather: List<WeatherApi>,
    val clouds: CloudsApi,
    val wind: WindApi
)