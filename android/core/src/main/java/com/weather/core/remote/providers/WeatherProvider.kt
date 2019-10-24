package com.weather.core.remote.providers

import com.weather.core.remote.models.open_weather_models.ForecastResponse
import com.weather.core.remote.models.open_weather_models.WeatherResponse
import io.reactivex.Single

interface WeatherProvider {
    fun fetchCurrentWeather(lat:Double,lon:Double): Single<WeatherResponse>

    fun getWeatherForecast(lat: Double, lon: Double): Single<ForecastResponse>
}