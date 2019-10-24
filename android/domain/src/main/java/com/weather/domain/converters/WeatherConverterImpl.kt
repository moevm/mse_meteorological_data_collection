package com.weather.domain.converters

import android.util.Log
import com.weather.core.remote.helpers.DateUtils
import com.weather.core.remote.helpers.toIntOrNull
import com.weather.core.remote.models.open_weather_models.ForecastApiItem
import com.weather.core.remote.models.open_weather_models.WeatherResponse
import com.weather.domain.models.CurrentWeather
import java.text.DateFormat
import java.util.*
import java.util.Calendar.HOUR_OF_DAY
import kotlin.math.abs
import kotlin.math.roundToInt

class WeatherConverterImpl : WeatherConverter {

    private val direction = listOf("C", "С-В", "В", "Ю-В", "Ю", "Ю-З", "З", "С-З")

    override fun convertFromWeatherResponseToCurrentWeather(weatherResponse: WeatherResponse): CurrentWeather {

        return with(weatherResponse) {
            CurrentWeather(
                "${main.temp}°",
                "${main.temp_max.toIntOrNull() ?: main.temp_max}",
                "${main.temp_min.toIntOrNull() ?: main.temp_min}",
                "${main.pressure} мбар",
                "${main.humidity}%",
                "${wind.speed} метр/сек",
                directionWind = direction[(((wind.deg % 360) / 45) % 8).roundToInt()],
                description = weather[0].description.capitalize(),
                name = name,
                iconUrl = "http://openweathermap.org/img/w/${weather.first().icon}.png",
                toolbarAlpha = calculateAlpha()
            )
        }
    }

    override fun convertFromForecastApiItemToCurrentWeather(forecastApiItem: ForecastApiItem): CurrentWeather {

        val date = Date(forecastApiItem.dt * 1000L)

        return with(forecastApiItem) {
            CurrentWeather(
                temp = "${main.temp}°",
                tempMax = "${main.temp_max.toIntOrNull() ?: main.temp_max}",
                tempMin = "${main.temp_min.toIntOrNull() ?: main.temp_min}",
                pressure = "${main.pressure} мбар",
                humidity = "${main.humidity}%",
                speedWind = "${wind.speed} метр/сек",
                directionWind = "",
                description = weather.first().description.capitalize().toString(),
                name = "",
                iconUrl = "http://openweathermap.org/img/w/${weather.first().icon}.png",
                toolbarAlpha = calculateAlpha(date.hours),
                dateString = android.text.format.DateFormat.format(DateUtils.defaultForecastFormat, date).toString()
            )
        }
    }

    private fun calculateAlpha(defH: Int? = null): Float {
        val h = defH ?: Calendar.getInstance().get(HOUR_OF_DAY)
        return when (h) {
            in 0..5, in 23..24 -> 1f
            in 6..11 -> 1f - (h.toFloat() / 12f)
            in 16..22 -> 1f - (h % 12) / 12f
            else -> 0f
        }
    }
}