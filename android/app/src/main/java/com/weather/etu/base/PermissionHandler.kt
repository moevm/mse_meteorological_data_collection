package com.weather.etu.base

interface PermissionHandler {
    fun withLocationPermission(action: () -> Unit)
}