package com.weather.etu.dagger.modules

import android.content.Context
import android.location.LocationManager
import dagger.Module
import dagger.Provides

@Module
class AppModule(val appContext: Context) {
    @Provides
    fun provideContext() = appContext

    @Provides
    fun provideLocationManager(ctx: Context)
            = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager
}