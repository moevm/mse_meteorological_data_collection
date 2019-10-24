package com.weather.core.remote.helpers

fun Double.toIntOrNull(): Int? = try { toInt() } catch (e: Exception) { null }