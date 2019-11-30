package com.weather.domain.repositories.current

import com.weather.domain.models.CurrentWeather
import io.reactivex.Single

interface CurrentWeatherRepository {

    fun fetchCurrentWeather(lat: Double, lon: Double): Single<CurrentWeather>

    fun fetchCurrentWeather(): Single<CurrentWeather>

    fun fetchWeatherForecast(): Single<List<CurrentWeather>>
}