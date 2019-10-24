package com.weather.etu.dagger.modules

import com.weather.core.remote.providers.WeatherProvider
import com.weather.core.remote.providers.WeatherProviderImpl
import com.weather.core.remote.services.OpenWeatherService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ProvidersModule {

    @Singleton
    @Provides
    fun provideWeatherProvider(openWeatherService: OpenWeatherService): WeatherProvider = WeatherProviderImpl(openWeatherService)
}