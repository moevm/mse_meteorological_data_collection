package com.weather.etu.dagger.modules

import android.content.Context
import android.content.SharedPreferences
import android.location.LocationManager
import com.weather.etu.model.CsvFileManager
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val appContext: Context) {
    @Provides
    fun provideContext() = appContext

    @Provides
    fun provideLocationManager(ctx: Context) =
        ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @Provides
    fun provideSharedPrefs(ctx: Context): SharedPreferences =
        ctx.getSharedPreferences(ctx.packageName, Context.MODE_PRIVATE)

    @Provides
    fun provideCsvFileManager() = CsvFileManager()
}