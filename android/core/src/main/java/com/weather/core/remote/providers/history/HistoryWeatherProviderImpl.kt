package com.weather.core.remote.providers.history

import android.util.Log
import com.weather.core.remote.models.history.DataWeather
import com.weather.core.remote.models.history.HistoryWeather
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.lang.Exception

class HistoryWeatherProviderImpl : HistoryWeatherProvider {

    companion object {
        const val BASE_URL_HISTORY = "https://www.gismeteo.ru/diary/"
        const val CALM = "Ш"
        val MAP_OVERCAST = mapOf(
            "sun.png" to "Ясно",
            "sunc.png" to "Малооблачно",
            "suncl.png" to "Облачно",
            "dull.png" to "Пасмурно"
        )
        val MAP_PHENOMENON = mapOf(
            "rain.png" to "Дождь",
            "snow.png" to "Снег",
            "storm.png" to "Гроза"
        )
    }

    override fun getQuarterHistoryWeather(
        indexCity: String,
        startYear: String,
        endYear: String
    ): HistoryWeather {
        fun averageForQuarter(year: String, range: IntRange): DataWeather? {
            val quarter = mutableListOf<DataWeather>()
            for (month in range) {
                val weatherForMonth = fetchWeatherForMonth(indexCity, year, month.toString())
                weatherForMonth?.let {
                    averageDataWeather(it.map { e -> e.second })?.let { dataWeather ->
                        quarter.add(dataWeather)
                    }
                }
            }
            return if (quarter.isNotEmpty()) {
                averageDataWeather(quarter)
            } else {
                null
            }
        }
        return HistoryWeather(
            mutableListOf<Pair<String, DataWeather>>().apply {
                for (i in 0..(endYear.toInt() - startYear.toInt())) {
                    val year = (startYear.toInt() + i).toString()
                    averageForQuarter(year, 1..3)?.let { this.add("$year-1 квартал" to it) }
                    averageForQuarter(year, 4..6)?.let { this.add(year + "-2 квартал" to it) }
                    averageForQuarter(year, 7..9)?.let { this.add("$year-3 квартал" to it) }
                    averageForQuarter(year, 10..12)?.let { this.add("$year-4 квартал" to it) }
                }
            }
        )
    }

    override fun getMonthlyHistoryWeather(
        indexCity: String,
        startYear: String,
        endYear: String
    ): HistoryWeather {
        return HistoryWeather(
            mutableListOf<Pair<String, DataWeather>>().apply {
                for (i in 0..(endYear.toInt() - startYear.toInt())) {
                    for (month in 1..12) {
                        val year = (startYear.toInt() + i).toString()
                        val weatherForMonth =
                            fetchWeatherForMonth(indexCity, year, month.toString())
                        weatherForMonth?.let {
                            averageDataWeather(it.map { e -> e.second })?.let { dataWeather ->
                                this.add("$year-$month" to dataWeather)
                            }
                        }
                    }
                }
            }
        )
    }

    override fun getDailyHistoryWeather(
        indexCity: String,
        startYear: String,
        endYear: String
    ): HistoryWeather {
        return HistoryWeather(
            mutableListOf<Pair<String, DataWeather>>().apply {
                for (i in 0..(endYear.toInt() - startYear.toInt())) {
                    for (month in 1..12) {
                        fetchWeatherForMonth(
                            indexCity,
                            (startYear.toInt() + i).toString(),
                            month.toString()
                        )?.let {
                            this.addAll(it)
                        }
                    }
                }
            }
        )
    }

    /*получить погоду за месяц(month) в таком-то году(year) в городе (indexCity)*/
    private fun fetchWeatherForMonth(
        indexCity: String,
        year: String,
        month: String
    ): List<Pair<String, DataWeather>>? {
        Log.d("HistoryWeather","$BASE_URL_HISTORY$indexCity/$year/$month/")
        try {
            val doc = Jsoup.connect("$BASE_URL_HISTORY$indexCity/$year/$month/").get()
            val tbodies = doc.select("table").select("tbody")
            return if (tbodies.isNotEmpty()) {
                mutableListOf<Pair<String, DataWeather>>().apply {
                    tbodies[1].select("tr").forEach {
                        val tds = it.select("td")
                        add(
                            year + "-" + month + "-" + tds[0].text() to DataWeather(
                                getTemperature(tds[1]),
                                getPressure(tds[2]),
                                getOvercast(tds[3]),
                                getPhenomenon(tds[4]),
                                getWind(tds[5]),
                                getDirectionWind(tds[5])
                            )
                        )
                    }
                }
            } else {
                null
            }
        }catch (e:Exception){
            return null
        }
    }

    private fun averageDataWeather(list: List<DataWeather>?): DataWeather? {
        return list?.let { dataWeather ->
            val directionWind =
                dataWeather.filter { it.directionWind != null }.groupBy { it.directionWind }
                    .maxBy { it.value.size }?.key
            val wind = if (directionWind == CALM) {
                "0"
            } else {
                dataWeather.filter { it.wind != null }.map { it.wind!!.toInt() }.average()
                    .toInt().toString()
            }
            DataWeather(
                dataWeather.filter { it.temperature != null }.map { it.temperature!!.toInt() }.average().toInt().toString(),
                dataWeather.filter { it.pressure != null }.map { it.pressure!!.toInt() }.average().toInt().toString(),
                dataWeather.filter { it.overcast != null }.groupBy { it.overcast }.maxBy { it.value.size }?.key,
                dataWeather.filter { it.phenomenon != null }.groupBy { it.phenomenon }.maxBy { it.value.size }?.key,
                wind,
                directionWind
            )
        }
    }

    private fun getTemperature(element: Element): String? {
        return if (element.text().isNotEmpty()) element.text() else null
    }

    private fun getPressure(element: Element): String? {
        return if (element.text().isNotEmpty()) element.text() else null
    }

    private fun getOvercast(element: Element): String? {
        val imgs = element.select("img")
        return if (imgs.isNotEmpty()) {
            MAP_OVERCAST[imgs[0].attr("src").split("/").last()]
        } else {
            null
        }
    }

    private fun getPhenomenon(element: Element): String? {
        val imgs = element.select("img")
        return if (imgs.isNotEmpty()) {
            MAP_PHENOMENON[imgs[0].attr("src").split("/").last()]
        } else {
            null
        }
    }

    /*Если штиль, то скорость ветра равна 0*/
    private fun getWind(element: Element): String? {
        val spans = element.select("span")
        return if (spans.isNotEmpty()) {
            try {
                val sbSpeed = StringBuilder()
                val speed = spans[0].text().split(" ")[1]
                for (ch in speed) {
                    if (ch.isDigit()) {
                        sbSpeed.append(ch)
                    }
                }
                sbSpeed.toString()
            } catch (e: IndexOutOfBoundsException) {
                return "0"
            }
        } else null
    }

    private fun getDirectionWind(element: Element): String? {
        val spans = element.select("span")
        return if (spans.isNotEmpty()) {
            spans[0].text().split(" ")[0]
        } else null
    }
}