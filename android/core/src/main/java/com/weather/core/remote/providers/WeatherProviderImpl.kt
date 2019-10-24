package com.weather.core.remote.providers

import android.util.Log
import com.weather.core.remote.helpers.RetrofitFactory
import com.weather.core.remote.models.open_weather_models.ForecastResponse
import com.weather.core.remote.models.open_weather_models.WeatherResponse
import com.weather.core.remote.services.OpenWeatherService
import io.reactivex.Single

class WeatherProviderImpl(
    private val openWeatherService: OpenWeatherService
): WeatherProvider {

    override fun fetchCurrentWeather(lat: Double, lon: Double): Single<WeatherResponse> {
        Log.d("WeatherResponseTest","$lat $lon")
        return openWeatherService.getCurrentWeather(lat,lon)
    }


    override fun getWeatherForecast(lat: Double, lon: Double): Single<ForecastResponse> {
        return openWeatherService.getWeatherForecast(lat, lon)
    }
}