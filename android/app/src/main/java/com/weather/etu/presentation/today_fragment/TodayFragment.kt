package com.weather.etu.presentation.today_fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.weather.etu.R
import com.weather.etu.presentation.BaseMainActivityFragment
import kotlinx.android.synthetic.main.fragment_today.*


class TodayFragment: BaseMainActivityFragment<TodayFragmentViewModel>() {

    private val adapter by lazy { ForecastAdapter() }

    override val toolbar by lazy { f1_toolbar as Toolbar }

    override val viewModel by lazy { ViewModelProviders
        .of(this)[TodayFragmentViewModel::class.java] }

    override val layoutId: Int
        get() = R.layout.fragment_today

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        withLocationPermission {
            viewModel.fetchCurrentWeather()
            viewModel.fetchWeatherForecast()
        }
    }

    @SuppressLint("SetTextI18n")
    override val bindViews = {
        _: View ->

        recycler.adapter = adapter

        viewModel.currentWeatherLiveData.observe(this, Observer {
            collapsing_toolbar.title = it.name
            tv_temperature.text = it.temp
            tv_today.text = it.currentDay
            tv_max.text = it.tempMax
            tv_min.text = it.tempMin
            tv_state_description.text = it.description
            toolbar_alpha.alpha = it.toolbarAlpha

            Glide
                .with(iv_weather_icon)
                .load(it.iconUrl)
                .into(iv_weather_icon)

        })

        viewModel.weatherForecastLiveData.observe(this, Observer {
            adapter.updateItems(it)
        })
    }
}