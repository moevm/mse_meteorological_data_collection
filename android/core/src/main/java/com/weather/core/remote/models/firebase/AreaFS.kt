package com.weather.core.remote.models.firebase

data class AreaFS(
    var name: String? = null,
    var cities: Map<String, String> = emptyMap()
)