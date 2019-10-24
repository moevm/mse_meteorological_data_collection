package com.weather.etu.base

import android.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

abstract class BaseActivity<T: BaseViewModel>: AppCompatActivity() {
    protected abstract val viewModel: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.errorLivaData.observe(this, Observer {
            showErrorSnackbar(
                window.decorView.findViewById(R.id.content),
                it
            )
        })
    }
}