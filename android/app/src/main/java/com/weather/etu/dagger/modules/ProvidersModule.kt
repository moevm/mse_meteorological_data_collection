package com.weather.etu.dagger.modules

import android.content.SharedPreferences
import com.weather.core.remote.providers.firestore.FirestoreProvider
import com.weather.core.remote.providers.firestore.FirestoreProviderImpl
import com.weather.core.remote.providers.current.CurrentWeatherProvider
import com.weather.core.remote.providers.current.CurrentWeatherProviderImpl
import com.weather.core.remote.providers.history.HistoryWeatherProvider
import com.weather.core.remote.providers.history.HistoryWeatherProviderImpl
import com.weather.core.remote.services.OpenWeatherService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ProvidersModule {

    @Singleton
    @Provides
    fun provideWeatherProvider(openWeatherService: OpenWeatherService): CurrentWeatherProvider =
        CurrentWeatherProviderImpl(openWeatherService)

    @Singleton
    @Provides
    fun provideFirebaseProvider(sharedPreferences: SharedPreferences): FirestoreProvider =
        FirestoreProviderImpl(sharedPreferences)

    @Singleton
    @Provides
    fun provideHistoryWeatherProvider():HistoryWeatherProvider =
        HistoryWeatherProviderImpl()
}