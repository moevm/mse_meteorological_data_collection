package com.weather.core.remote.models

data class ParseRequest(
    val cityName: String = "",
    val cityCode: String = "",
    val interval: Interval = Interval.DAY,
    val startDateMil: Long = System.currentTimeMillis(),
    val endDateMil: Long = System.currentTimeMillis()
)