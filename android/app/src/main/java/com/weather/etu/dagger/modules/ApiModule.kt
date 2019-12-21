package com.weather.etu.dagger.modules

import com.weather.core.remote.helpers.ApiBuilder
import com.weather.core.remote.helpers.Constants.Companion.BASE_URL_OPEN_WEATHER
import com.weather.core.remote.helpers.RetrofitFactory
import com.weather.core.remote.services.OpenWeatherService
import dagger.Module
import dagger.Provides
import javax.inject.Named


@Module
class ApiModule {

    companion object {
        private const val WEATHER_URL_NAME = "weather_api"
    }

    @Provides
    @Named(WEATHER_URL_NAME)
    fun provideWeatherUrl() = BASE_URL_OPEN_WEATHER

    @Provides
    fun provideWeatherApiBuilder(@Named(WEATHER_URL_NAME) url: String): ApiBuilder = RetrofitFactory(url)

    @Provides
    fun provideOpenWeatherService(apiBuilder: ApiBuilder) = apiBuilder.buildApi(OpenWeatherService::class.java)
}