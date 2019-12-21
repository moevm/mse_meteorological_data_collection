package com.weather.core.remote.models.history

data class DataWeather(
    val temperature:String?,
    val pressure:String?,
    val overcast:String?,
    val phenomenon:String?,
    val wind:String?,
    val directionWind:String?
)