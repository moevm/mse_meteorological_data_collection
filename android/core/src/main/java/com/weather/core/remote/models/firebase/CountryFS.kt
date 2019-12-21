package com.weather.core.remote.models.firebase

data class CountryFS(
    var countryName: String? = null,
    var areas: List<String> = emptyList()
) {
    override fun toString(): String {
        return this.countryName ?: super.toString()
    }
}