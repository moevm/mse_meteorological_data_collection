package com.weather.domain.repositories

import android.annotation.SuppressLint
import android.location.LocationManager
import com.weather.core.remote.providers.WeatherProvider
import com.weather.domain.converters.WeatherConverter
import com.weather.domain.models.CurrentWeather
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class WeatherRepositoryImpl(
    private val weatherProvider: WeatherProvider,
    private val weatherConverter: WeatherConverter,
    private val locationManager: LocationManager
) : WeatherRepository {

    override fun fetchCurrentWeather(lat: Double, lon: Double): Single<CurrentWeather> {
        return weatherProvider.fetchCurrentWeather(lat, lon)
            .map { weatherConverter.convertFromWeatherResponseToCurrentWeather(it) }
            .subscribeOn(Schedulers.io())
    }

    @SuppressLint("MissingPermission")
    override fun fetchWeatherForecast(): Single<List<CurrentWeather>> {
        return Single
            .just(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER))
            .flatMap { weatherProvider.getWeatherForecast(it.latitude, it.longitude) }
            .map { it.list.map { weatherConverter.convertFromForecastApiItemToCurrentWeather(it) } }
            .subscribeOn(Schedulers.io())
    }

    @SuppressLint("MissingPermission")
    override fun fetchCurrentWeather(): Single<CurrentWeather> {
        return Single
            .just(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER))
            .flatMap { fetchCurrentWeather(it.latitude, it.longitude) }
            .zipWith(getCurrentDay(), BiFunction { t1: CurrentWeather, t2: String ->
                t1.copy(currentDay = t2.capitalize())
            })
            .subscribeOn(Schedulers.io())
    }



    private fun getCurrentDay(): Single<String> = Single.just(
        SimpleDateFormat("EEEE", Locale.getDefault())
            .format(Calendar.getInstance().time).toString())
}