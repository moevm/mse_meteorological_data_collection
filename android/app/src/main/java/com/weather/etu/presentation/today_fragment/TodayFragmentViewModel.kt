package com.weather.etu.presentation.today_fragment

import androidx.lifecycle.MutableLiveData
import com.weather.domain.models.CurrentWeather
import com.weather.domain.repositories.current.CurrentWeatherRepository
import com.weather.etu.app.App
import com.weather.etu.base.BaseViewModel
import javax.inject.Inject

class TodayFragmentViewModel: BaseViewModel() {

    @Inject
    lateinit var repositoryCurrent: CurrentWeatherRepository

    val currentWeatherLiveData = MutableLiveData<CurrentWeather>()
    val weatherForecastLiveData = MutableLiveData<List<CurrentWeather>>()

    init {
        App.component.inject(this)
    }

    fun fetchCurrentWeather(){
        disposable.add(
            repositoryCurrent.fetchCurrentWeather()
                .safeSubscribe(currentWeatherLiveData::postValue)
        )
    }

    fun fetchWeatherForecast() {
        disposable.add(
            repositoryCurrent.fetchWeatherForecast()
                .safeSubscribe(weatherForecastLiveData::postValue)
        )
    }
}