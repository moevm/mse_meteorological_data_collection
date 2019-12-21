package com.weather.etu.presentation.chart_activity

import androidx.lifecycle.MutableLiveData
import com.weather.core.remote.models.history.HistoryWeather
import com.weather.etu.app.App
import com.weather.etu.base.BaseViewModel
import com.weather.etu.model.CsvFileManager
import java.io.File
import javax.inject.Inject

class ChartActivityViewModel:BaseViewModel() {

    @Inject
    lateinit var csvFileManager: CsvFileManager

    val  historyWeather = MutableLiveData<HistoryWeather?>()

    init {
        App.component.inject(this)
    }

    fun fetchHistoryWeather(name:String){
        historyWeather.postValue(null)
        disposable.add(
            csvFileManager.fetchHistoryWeatherFromFile(name)
                .safeSubscribe {
                    historyWeather.postValue(it)
                }
        )
    }
}