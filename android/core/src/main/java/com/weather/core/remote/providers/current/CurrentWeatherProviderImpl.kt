package com.weather.core.remote.providers.current

import android.util.Log
import com.weather.core.remote.models.open_weather_models.ForecastResponse
import com.weather.core.remote.models.open_weather_models.WeatherResponse
import com.weather.core.remote.services.OpenWeatherService
import io.reactivex.Single

class CurrentWeatherProviderImpl(
    private val openWeatherService: OpenWeatherService
): CurrentWeatherProvider {

    override fun getCurrentWeather(lat: Double, lon: Double): Single<WeatherResponse> {
        Log.d("WeatherResponseTest","$lat $lon")
        return openWeatherService.getCurrentWeather(lat,lon)
    }


    override fun getForecastWeather(lat: Double, lon: Double): Single<ForecastResponse> {
        return openWeatherService.getWeatherForecast(lat, lon)
    }
}