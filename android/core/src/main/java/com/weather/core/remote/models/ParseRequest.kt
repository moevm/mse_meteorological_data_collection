package com.weather.core.remote.models

data class ParseRequest(
    val cityCode: String = "",
    val interval: Interval = Interval.DAY,
    val startDateMil: Long = System.currentTimeMillis(),
    val endDatemil: Long = System.currentTimeMillis()
)