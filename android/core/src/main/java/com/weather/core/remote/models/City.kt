package com.weather.core.remote.models

data class City(
    val name: String,
    val index: String
) {
    override fun toString(): String {
        return name
    }
}