package com.weather.etu.presentation.interval_fragment

import com.weather.etu.app.App
import com.weather.etu.base.BaseViewModel

class IntervalFragmentViewModel: BaseViewModel() {

    init {
        App.component.inject(this)
    }
}