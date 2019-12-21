package com.weather.etu.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import com.weather.etu.app.App
import com.weather.etu.base.BaseFragment
import com.weather.etu.base.BaseViewModel
import com.weather.etu.base.PermissionHandler

abstract class BaseMainActivityFragment<T: BaseViewModel>: BaseFragment<T>(), PermissionHandler {

    companion object {
        private const val LOCATION_RC = 99
    }

    protected open val toolbar: Toolbar? = null

    private val locationActions = arrayListOf<() -> Unit>()

    protected val navViewModel by lazy { ViewModelProviders
        .of(activity as MainActivity)[MainActivityViewModel::class.java] }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).setSupportActionBar(toolbar?:return)
    }

    protected fun setTitle(title: String) {
        (activity as AppCompatActivity).supportActionBar?.title = title
    }

    override fun withLocationPermission(action: () -> Unit) {
        if (locationPermissionGranted() && foregroundServicePermissionGranted()) {
            locationActions.clear()
            action.invoke()
        } else {
            locationActions.add(action)
            val permissions = arrayListOf(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                permissions.add(Manifest.permission.FOREGROUND_SERVICE)

            requestPermissions(permissions.toTypedArray(), LOCATION_RC)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (locationPermissionGranted() && foregroundServicePermissionGranted()) {
            locationActions.forEach { it.invoke() }
        } else {
            locationActions.clear()
        }
    }

    private fun locationPermissionGranted() = ActivityCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

    private fun foregroundServicePermissionGranted() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ActivityCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.FOREGROUND_SERVICE
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }
}