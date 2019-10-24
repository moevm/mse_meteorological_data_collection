package com.weather.etu.presentation.interval_fragment

import androidx.lifecycle.ViewModelProviders
import com.weather.etu.R
import com.weather.etu.presentation.BaseMainActivityFragment

class IntervalFragment: BaseMainActivityFragment<IntervalFragmentViewModel>() {

    override val viewModel by lazy { ViewModelProviders
        .of(this)[IntervalFragmentViewModel::class.java] }

    override val layoutId: Int
        get() = R.layout.fragment_interval
}