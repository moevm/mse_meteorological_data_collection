package com.weather.etu.presentation.interval_fragment

import androidx.lifecycle.MutableLiveData
import com.weather.core.remote.models.firebase.CountryFS
import com.weather.domain.repositories.FiresoreRepository
import com.weather.etu.app.App
import com.weather.etu.base.BaseViewModel
import com.weather.core.remote.models.Interval
import com.weather.core.remote.models.ParseRequest
import java.util.*
import javax.inject.Inject

class IntervalFragmentViewModel : BaseViewModel() {

    @Inject
    lateinit var firesoreRepository: FiresoreRepository

    val countriesLD = MutableLiveData<List<CountryFS>>()
    val areasLD = MutableLiveData<List<String>>()
    val citiesLD = MutableLiveData<List<Pair<String, String>>>()
    val startDateLD = MutableLiveData<Calendar>().apply { postValue(Calendar.getInstance()) }
    val endDateLD = MutableLiveData<Calendar>().apply { postValue(Calendar.getInstance()) }

    private var request = ParseRequest()

    init {
        App.component.inject(this)
    }

    fun fetchCountries() {
        disposable.add(
            firesoreRepository
                .getCountries()
                .safeSubscribe(countriesLD::postValue)
        )
    }

    fun onCountrySelected(country: CountryFS) {
        areasLD.postValue(country.areas)
        onAreaSelected(country.areas.first())
    }

    fun onAreaSelected(area: String) {
        disposable.add(
            firesoreRepository.getArea(area)
                .safeSubscribe {
                    val lst = it.cities.toList()
                    onCitySelected(lst.first())
                    citiesLD.postValue(lst)
                }
        )
    }

    fun onCitySelected(city: Pair<String, String>) {
        request = request.copy(cityCode = city.second)
    }

    fun onIntervalSelected(interval: Interval) {
        request = request.copy(interval = interval)
    }

    fun getRequestParams() = request.copy()

    fun updateStartDate(time: Calendar) {
        request = request.copy(startDateMil = time.timeInMillis)
        startDateLD.postValue(time)
    }

    fun updateEndDate(time: Calendar) {
        request = request.copy(endDatemil = time.timeInMillis)
        endDateLD.postValue(time)
    }

    // TODO: сделать парсинг
    fun onSubmit() {
        when (request.interval) {
            Interval.DAY -> {
            }
            Interval.MONTH -> {
            }
            Interval.YEAR -> {
            }
            Interval.SEASON -> {
            }
        }
    }

}