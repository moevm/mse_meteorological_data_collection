package com.weather.core.remote.models

enum class Interval(name: String) {
    DAY("День"),
    MONTH("Месяц"),
    QUARTER("Квартал");

    private val _name: String = name

    override fun toString(): String {
        return _name
    }
}