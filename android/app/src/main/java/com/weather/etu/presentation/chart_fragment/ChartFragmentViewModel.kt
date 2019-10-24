package com.weather.etu.presentation.chart_fragment

import com.weather.etu.app.App
import com.weather.etu.base.BaseViewModel

class ChartFragmentViewModel: BaseViewModel() {
    init {
        App.component.inject(this)
    }
}