package com.weather.etu.presentation.today_fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.weather.domain.models.CurrentWeather
import com.weather.etu.R
import com.weather.etu.base.BaseAdapter
import kotlinx.android.synthetic.main.item_weather_forecast.view.*

class ForecastAdapter: BaseAdapter<CurrentWeather>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<CurrentWeather> {
        return ForecastViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_weather_forecast, parent, false))
    }

    private inner class ForecastViewHolder(v: View): BaseAdapter.BaseViewHolder<CurrentWeather>(v) {
        override fun bind(model: CurrentWeather) {
            with(itemView) {
                tv_date.text = model.dateString
                tv_temperature.text = model.temp
                bg_alpha.alpha = model.toolbarAlpha

                Glide
                    .with(iv_weather_icon)
                    .load(model.iconUrl)
                    .into(iv_weather_icon)
            }
        }
    }
}