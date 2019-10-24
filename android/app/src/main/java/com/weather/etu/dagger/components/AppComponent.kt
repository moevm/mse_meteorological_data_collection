package com.weather.etu.dagger.components

import com.weather.etu.dagger.modules.ApiModule
import com.weather.etu.dagger.modules.AppModule
import com.weather.etu.dagger.modules.RepositoriesModule
import com.weather.etu.app.App
import com.weather.etu.dagger.modules.ProvidersModule
import com.weather.etu.presentation.MainActivityViewModel
import com.weather.etu.presentation.chart_fragment.ChartFragmentViewModel
import com.weather.etu.presentation.interval_fragment.IntervalFragmentViewModel
import com.weather.etu.presentation.today_fragment.TodayFragmentViewModel
import dagger.Component
import javax.inject.Singleton

@Component(modules = [
    AppModule::class,
    ProvidersModule::class,
    RepositoriesModule::class,
    ApiModule::class
])
@Singleton
interface AppComponent {
    fun inject(obj: App)

    fun inject(obj: MainActivityViewModel)

    fun inject(obj: ChartFragmentViewModel)

    fun inject(obj: IntervalFragmentViewModel)

    fun inject(obj: TodayFragmentViewModel)
}