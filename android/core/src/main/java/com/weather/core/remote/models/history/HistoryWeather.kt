package com.weather.core.remote.models.history

import com.weather.core.remote.models.history.DataWeather

data class HistoryWeather(
    val historyWeather: List<Pair<String, DataWeather>>
)