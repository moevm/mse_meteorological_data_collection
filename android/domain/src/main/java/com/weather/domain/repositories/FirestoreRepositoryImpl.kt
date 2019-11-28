package com.weather.domain.repositories

import com.weather.core.remote.models.firebase.AreaFS
import com.weather.core.remote.providers.FirestoreProvider
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class FirestoreRepositoryImpl(
    private val firestoreProvider: FirestoreProvider
) : FiresoreRepository {

    override fun getCountries() = firestoreProvider.fetchCountries().subscribeOn(Schedulers.io())

    override fun getAreas() = firestoreProvider.fetchAreas().subscribeOn(Schedulers.io())

    override fun getArea(name: String): Single<AreaFS> =
        firestoreProvider.getAreaByName(name).subscribeOn(Schedulers.io())
}