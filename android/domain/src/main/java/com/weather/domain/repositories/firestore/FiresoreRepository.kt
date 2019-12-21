package com.weather.domain.repositories.firestore

import com.weather.core.remote.models.firebase.AreaFS
import com.weather.core.remote.models.firebase.CountryFS
import io.reactivex.Maybe
import io.reactivex.Single

interface FiresoreRepository {
    fun fetchCountries(): Single<List<CountryFS>>

    fun fetchAreas(): Single<List<AreaFS>>

    fun fetchArea(name: String): Single<AreaFS>
}