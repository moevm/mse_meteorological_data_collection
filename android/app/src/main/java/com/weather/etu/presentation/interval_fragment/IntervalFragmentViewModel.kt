package com.weather.etu.presentation.interval_fragment

import androidx.lifecycle.MutableLiveData
import com.weather.core.remote.models.City
import com.weather.core.remote.models.firebase.CountryFS
import com.weather.domain.repositories.firestore.FiresoreRepository
import com.weather.etu.app.App
import com.weather.etu.base.BaseViewModel
import com.weather.core.remote.models.Interval
import com.weather.core.remote.models.ParseRequest
import com.weather.domain.repositories.history.HistoryWeatherRepository
import com.weather.etu.base.getYearFromMil
import com.weather.etu.model.CsvFileManager
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class IntervalFragmentViewModel : BaseViewModel() {

    @Inject
    lateinit var firesoreRepository: FiresoreRepository
    @Inject
    lateinit var historyWeatherRepository: HistoryWeatherRepository
    @Inject
    lateinit var csvFileManager: CsvFileManager

    val countriesLD = MutableLiveData<List<CountryFS>>()
    val areasLD = MutableLiveData<List<String>>()
    val citiesLD = MutableLiveData<List<City>>()
    val startDateLD = MutableLiveData<Calendar>().apply { postValue(Calendar.getInstance()) }
    val endDateLD = MutableLiveData<Calendar>().apply { postValue(Calendar.getInstance()) }
    val fileHistoryWeatherLD = MutableLiveData<File?>()

    private var request = ParseRequest()

    init {
        App.component.inject(this)
    }

    fun fetchCountries() {
        disposable.add(
            firesoreRepository
                .fetchCountries()
                .safeSubscribe(countriesLD::postValue)
        )
    }

    fun onCountrySelected(country: CountryFS) {
        areasLD.postValue(country.areas)
        onAreaSelected(country.areas.first())
    }

    fun onAreaSelected(area: String) {
        disposable.add(
            firesoreRepository.fetchArea(area)
                .safeSubscribe {
                    val lst = it.cities.toList().map { City(it.first, it.second) }
                    onCitySelected(lst.first())
                    citiesLD.postValue(lst)
                }
        )
    }

    fun onCitySelected(city: City) {
        request = request.copy(cityName = city.name, cityCode = city.index)
    }

    fun onIntervalSelected(interval: Interval) {
        request = request.copy(interval = interval)
    }

    fun getRequestParams() = request.copy()

    fun updateStartDate(time: Calendar) {
        val format = SimpleDateFormat("yyyy")
        request = request.copy(startDateMil = time.timeInMillis)
        startDateLD.postValue(time)
    }

    fun updateEndDate(time: Calendar) {
        val format = SimpleDateFormat("yyyy")
        request = request.copy(endDateMil = time.timeInMillis)
        endDateLD.postValue(time)
    }

    //TODO учитывать не только года, но и месяц и день при парсинги. Сейчас ищет только начиная от кого-то года до какого-то ( пример 2017-2019)
    fun onSubmit() {
        fileHistoryWeatherLD.postValue(null)
        val startYear = request.startDateMil.getYearFromMil()
        val endYear = request.endDateMil.getYearFromMil()
        disposable.add(
            when (request.interval) {
                Interval.DAY -> {
                    historyWeatherRepository.fetchDailyHistoryWeather(
                        request.cityCode,
                        startYear,
                        endYear
                    )
                }
                Interval.MONTH -> {
                    historyWeatherRepository.fetchMonthlyHistoryWeather(
                        request.cityCode,
                        startYear,
                        endYear
                    )
                }
                Interval.QUARTER -> {
                    historyWeatherRepository.fetchQuarterHistoryWeather(
                        request.cityCode,
                        startYear,
                        endYear
                    )
                }
            }.flatMap {
                csvFileManager.saveHistoryWeatherInFile(it, request)
            }.safeSubscribe {
                fileHistoryWeatherLD.postValue(it)
            }
        )
    }
}