package com.weather.etu.dagger.modules

import android.location.LocationManager
import com.weather.core.remote.providers.firestore.FirestoreProvider
import com.weather.core.remote.providers.current.CurrentWeatherProvider
import com.weather.core.remote.providers.history.HistoryWeatherProvider
import com.weather.domain.converters.WeatherConverter
import com.weather.domain.converters.WeatherConverterImpl
import com.weather.domain.repositories.firestore.FiresoreRepository
import com.weather.domain.repositories.firestore.FirestoreRepositoryImpl
import com.weather.domain.repositories.current.CurrentWeatherRepository
import com.weather.domain.repositories.current.CurrentWeatherRepositoryImpl
import com.weather.domain.repositories.history.HistoryWeatherRepository
import com.weather.domain.repositories.history.HistoryWeatherRepositoryImpl
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
        providerCurrent: CurrentWeatherProvider,
        converter: WeatherConverter,
        locationManager: LocationManager
    ): CurrentWeatherRepository {
        return CurrentWeatherRepositoryImpl(
            providerCurrent,
            converter,
            locationManager
        )
    }

    @Singleton
    @Provides
    fun provideFirestoreRepository(
        firestoreProvider: FirestoreProvider
    ): FiresoreRepository =
        FirestoreRepositoryImpl(firestoreProvider)

    @Singleton
    @Provides
    fun provideHistoryWeatherRepository(
        historyWeatherProvider: HistoryWeatherProvider
    ):HistoryWeatherRepository =
        HistoryWeatherRepositoryImpl(historyWeatherProvider)
}