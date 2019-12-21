package com.weather.core.remote.providers.firestore

import com.weather.core.remote.models.firebase.AreaFS
import com.weather.core.remote.models.firebase.CountryFS
import io.reactivex.Maybe
import io.reactivex.Single

interface FirestoreProvider {
    fun getCountries(): Single<List<CountryFS>>

    fun getAreas(): Single<List<AreaFS>>

    fun getAreaByName(name: String): Single<AreaFS>
}