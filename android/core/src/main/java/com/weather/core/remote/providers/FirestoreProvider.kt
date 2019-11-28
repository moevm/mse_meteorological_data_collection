package com.weather.core.remote.providers

import com.weather.core.remote.models.firebase.AreaFS
import com.weather.core.remote.models.firebase.CountryFS
import io.reactivex.Maybe
import io.reactivex.Single

interface FirestoreProvider {
    fun fetchCountries(): Single<List<CountryFS>>

    fun fetchAreas(): Single<List<AreaFS>>

    fun getAreaByName(name: String): Single<AreaFS>
}