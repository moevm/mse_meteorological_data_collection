package com.weather.etu.dagger.modules

import android.content.SharedPreferences
import com.google.firebase.FirebaseApp
import com.weather.core.remote.providers.FirestoreProvider
import com.weather.core.remote.providers.FirestoreProviderImpl
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
    fun provideWeatherProvider(openWeatherService: OpenWeatherService): WeatherProvider =
        WeatherProviderImpl(openWeatherService)

    @Singleton
    @Provides
    fun provideFirebaseProvider(sharedPreferences: SharedPreferences): FirestoreProvider =
        FirestoreProviderImpl(sharedPreferences)
}