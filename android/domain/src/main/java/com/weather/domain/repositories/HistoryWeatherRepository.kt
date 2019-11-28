package com.weather.domain.repositories

import com.weather.domain.models.history.HistoryWeather

interface HistoryWeatherRepository {

    fun fetchQuarterHistoryWeather(
        indexCity: String,
        startYear: String,
        endYear: String
    ): HistoryWeather

    fun fetchMonthlyHistoryWeather(
        indexCity: String,
        startYear: String,
        endYear: String
    ): HistoryWeather

    fun fetchDailyHistoryWeather(
        indexCity: String,
        startYear: String,
        endYear: String
    ): HistoryWeather

}