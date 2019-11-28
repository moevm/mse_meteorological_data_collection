package com.weather.domain.repositories

import com.weather.core.remote.models.firebase.AreaFS
import com.weather.core.remote.models.firebase.CountryFS
import io.reactivex.Maybe
import io.reactivex.Single

interface FiresoreRepository {
    fun getCountries(): Single<List<CountryFS>>

    fun getAreas(): Single<List<AreaFS>>

    fun getArea(name: String): Single<AreaFS>
}