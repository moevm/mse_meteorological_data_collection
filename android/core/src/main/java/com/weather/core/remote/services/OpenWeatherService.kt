package com.weather.core.remote.services

import com.weather.core.remote.helpers.Constants.Companion.KEY_OPEN_WEATHER
import com.weather.core.remote.models.open_weather_models.ForecastResponse
import com.weather.core.remote.models.open_weather_models.WeatherResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherService {

    @GET("weather?units=metric&lang=ru&APPID=$KEY_OPEN_WEATHER")
    fun getCurrentWeather(
        @Query("lat") lat:Double,
        @Query("lon") lon:Double
    ) : Single<WeatherResponse>

    @GET("forecast?units=metric&lang=ru&APPID=$KEY_OPEN_WEATHER")
    fun getWeatherForecast(
        @Query("lat") lat:Double,
        @Query("lon") lon:Double
    ) : Single<ForecastResponse>
}
