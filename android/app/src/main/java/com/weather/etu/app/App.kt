package com.weather.etu.app

import android.app.Application
import com.google.firebase.FirebaseApp
import com.weather.etu.dagger.modules.AppModule
import com.weather.etu.dagger.modules.RepositoriesModule
import com.weather.etu.dagger.components.AppComponent
import com.weather.etu.dagger.components.DaggerAppComponent
import com.weather.etu.dagger.modules.ApiModule

class App : Application() {

    companion object {
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        component = buildComponent()
        component.inject(this)
    }

    private fun buildComponent(): AppComponent = DaggerAppComponent.builder()
        .appModule(AppModule(applicationContext))
//        .databaseModule(DatabaseModule())
        .apiModule(ApiModule())
        .repositoriesModule(RepositoriesModule())
        .build()
}