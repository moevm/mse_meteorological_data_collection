package com.weather.etu.presentation.statistic_activity

import androidx.lifecycle.MutableLiveData
import com.weather.core.remote.models.history.DataWeather
import com.weather.etu.app.App
import com.weather.etu.base.BaseViewModel
import com.weather.etu.model.CsvFileManager
import com.weather.etu.model.Statistic
import javax.inject.Inject

class StatisticActivityViewModel:BaseViewModel() {

    @Inject
    lateinit var csvFileManager: CsvFileManager

    val  statistic = MutableLiveData<Statistic?>()

    init {
        App.component.inject(this)
    }

    fun fetchHistoryWeather(name:String){
        statistic.postValue(null)
        disposable.add(
            csvFileManager.fetchHistoryWeatherFromFile(name)
                .map { getStatistic(it.historyWeather) }
                .safeSubscribe { statistic.postValue(it) }
        )
    }

    private fun getStatistic(historyWeather: List<Pair<String, DataWeather>>):Statistic{
        val temperatures = historyWeather
            .filter { it.second.temperature!= "-" && it.second.temperature!=null}
            .map { it.second.temperature!!.toInt()}

        val pressures = historyWeather
            .filter { it.second.pressure!="-" && it.second.pressure!=null }
            .map { it.second.pressure!!.toInt() }

        val wind = historyWeather
            .filter { it.second.wind!="-" && it.second.wind!=null }
            .map { it.second.wind!!.toInt() }

        return Statistic(
            temperatures.average().toInt(),
            temperatures.min()!!.toInt(),
            temperatures.max()!!.toInt(),
            temperatures.groupBy { it }.maxBy { it.value.size }!!.key,
            pressures.average().toInt(),
            pressures.min()!!.toInt(),
            pressures.max()!!.toInt(),
            pressures.groupBy { it }.maxBy { it.value.size }!!.key,
            wind.average().toInt(),
            wind.min()!!.toInt(),
            wind.max()!!.toInt(),
            wind.groupBy { it }.maxBy { it.value.size }!!.key
        )
    }
}