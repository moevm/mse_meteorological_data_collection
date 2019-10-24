package com.weather.etu.presentation

import androidx.lifecycle.MutableLiveData
import com.weather.etu.app.App
import com.weather.etu.base.BaseViewModel
import com.weather.etu.presentation.MainActivity.Companion.STATE
import com.weather.etu.presentation.MainActivity.Companion.STATE.*

class MainActivityViewModel: BaseViewModel() {

    private var currentState: STATE? = null

    val stateLiveData = MutableLiveData<STATE>()

    init {
        App.component.inject(this)
        changeState(TODAY)
    }

    fun onTodayClicked() = changeState(TODAY)

    fun onIntervalCLicked() = changeState(INTERVAL)

    fun onChartClicked() = changeState(CHART)

    private fun changeState(state: STATE, action: (() -> Unit)? = null): Boolean {
        if (state != currentState) {
            currentState = state
            stateLiveData.postValue(state)
            action?.invoke()
            return true
        }
        return false
    }
}