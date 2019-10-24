package com.weather.etu.dagger.modules

import android.location.LocationManager
import com.weather.core.remote.providers.WeatherProvider
import com.weather.domain.converters.WeatherConverter
import com.weather.domain.converters.WeatherConverterImpl
import com.weather.domain.repositories.WeatherRepository
import com.weather.domain.repositories.WeatherRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoriesModule {

    @Singleton
    @Provides
    fun provideWeatherConverter(): WeatherConverter = WeatherConverterImpl()

    @Singleton
    @Provides
    fun provideWeatherRepository(
        provider: WeatherProvider,
        converter: WeatherConverter,
        locationManager: LocationManager
    ): WeatherRepository {
        return WeatherRepositoryImpl(provider, converter, locationManager)
    }
}