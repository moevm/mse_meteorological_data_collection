package com.weather.etu.base

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun showErrorSnackbar(v: View, t: Throwable)
        = Snackbar.make(v, "Error: $t", Snackbar.LENGTH_LONG).show()
