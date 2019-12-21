package com.weather.etu.dagger.components

import com.weather.etu.dagger.modules.ApiModule
import com.weather.etu.dagger.modules.AppModule
import com.weather.etu.dagger.modules.RepositoriesModule
import com.weather.etu.app.App
import com.weather.etu.dagger.modules.ProvidersModule
import com.weather.etu.presentation.MainActivityViewModel
import com.weather.etu.presentation.chart_activity.ChartActivityViewModel
import com.weather.etu.presentation.files_fragment.FilesFragmentViewModel
import com.weather.etu.presentation.interval_fragment.IntervalFragmentViewModel
import com.weather.etu.presentation.statistic_activity.StatisticActivity
import com.weather.etu.presentation.statistic_activity.StatisticActivityViewModel
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

    fun inject(obj: ChartActivityViewModel)

    fun inject(obj: StatisticActivityViewModel)

    fun inject(obj: IntervalFragmentViewModel)

    fun inject(obj: TodayFragmentViewModel)

    fun inject(obj: FilesFragmentViewModel)
}