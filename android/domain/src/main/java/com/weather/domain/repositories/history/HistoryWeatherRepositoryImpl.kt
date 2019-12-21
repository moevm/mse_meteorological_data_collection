package com.weather.domain.repositories.history

import com.weather.core.remote.models.history.HistoryWeather
import com.weather.core.remote.providers.history.HistoryWeatherProvider
import io.reactivex.Single

class HistoryWeatherRepositoryImpl(
    private val historyWeatherProvider: HistoryWeatherProvider
) :HistoryWeatherRepository {

    override fun fetchQuarterHistoryWeather(
        indexCity: String,
        startYear: String,
        endYear: String
    ): Single<HistoryWeather> {
        return Single.create {
            it.onSuccess(historyWeatherProvider.getQuarterHistoryWeather(indexCity,startYear,endYear))
        }
    }

    override fun fetchMonthlyHistoryWeather(
        indexCity: String,
        startYear: String,
        endYear: String
    ): Single<HistoryWeather> {
        return Single.create {
            it.onSuccess(historyWeatherProvider.getMonthlyHistoryWeather(indexCity,startYear,endYear))
        }
    }

    override fun fetchDailyHistoryWeather(
        indexCity: String,
        startYear: String,
        endYear: String
    ): Single<HistoryWeather> {
        return Single.create {
            it.onSuccess(historyWeatherProvider.getDailyHistoryWeather(indexCity,startYear,endYear))
        }
    }
}