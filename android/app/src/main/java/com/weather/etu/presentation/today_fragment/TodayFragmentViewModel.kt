package com.weather.etu.presentation.today_fragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.weather.domain.models.CurrentWeather
import com.weather.domain.repositories.WeatherRepository
import com.weather.etu.app.App
import com.weather.etu.base.BaseViewModel
import javax.inject.Inject

class TodayFragmentViewModel: BaseViewModel() {

    @Inject
    lateinit var repository: WeatherRepository

    val currentWeatherLiveData = MutableLiveData<CurrentWeather>()
    val weatherForecastLiveData = MutableLiveData<List<CurrentWeather>>()

    init {
        App.component.inject(this)
    }

    fun fetchCurrentWeather(){
        disposable.add(
            repository.fetchCurrentWeather()
                .safeSubscribe(currentWeatherLiveData::postValue)
        )
    }

    fun fetchWeatherForecast() {
        disposable.add(
            repository.fetchWeatherForecast()
                .safeSubscribe(weatherForecastLiveData::postValue)
        )
    }
}