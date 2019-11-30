package com.weather.domain.repositories.history

import com.weather.core.remote.models.history.HistoryWeather
import io.reactivex.Single

interface HistoryWeatherRepository {

    fun fetchQuarterHistoryWeather(
        indexCity: String,
        startYear: String,
        endYear: String
    ): Single<HistoryWeather>

    fun fetchMonthlyHistoryWeather(
        indexCity: String,
        startYear: String,
        endYear: String
    ): Single<HistoryWeather>

    fun fetchDailyHistoryWeather(
        indexCity: String,
        startYear: String,
        endYear: String
    ): Single<HistoryWeather>
}