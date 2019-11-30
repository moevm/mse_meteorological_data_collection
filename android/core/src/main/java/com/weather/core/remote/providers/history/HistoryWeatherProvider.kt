package com.weather.core.remote.providers.history

import com.weather.core.remote.models.history.HistoryWeather

interface HistoryWeatherProvider {

    fun getQuarterHistoryWeather(
        indexCity: String,
        startYear: String,
        endYear: String
    ): HistoryWeather

    fun getMonthlyHistoryWeather(
        indexCity: String,
        startYear: String,
        endYear: String
    ): HistoryWeather

    fun getDailyHistoryWeather(
        indexCity: String,
        startYear: String,
        endYear: String
    ): HistoryWeather

}