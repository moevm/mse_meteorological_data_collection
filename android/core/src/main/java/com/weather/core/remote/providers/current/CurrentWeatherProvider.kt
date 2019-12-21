package com.weather.core.remote.providers.current

import com.weather.core.remote.models.open_weather_models.ForecastResponse
import com.weather.core.remote.models.open_weather_models.WeatherResponse
import io.reactivex.Single

interface CurrentWeatherProvider {
    fun getCurrentWeather(lat:Double, lon:Double): Single<WeatherResponse>

    fun getForecastWeather(lat: Double, lon: Double): Single<ForecastResponse>
}