package com.weather.domain.repositories.firestore

import com.weather.core.remote.models.firebase.AreaFS
import com.weather.core.remote.providers.firestore.FirestoreProvider
import com.weather.domain.repositories.firestore.FiresoreRepository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class FirestoreRepositoryImpl(
    private val firestoreProvider: FirestoreProvider
) : FiresoreRepository {

    override fun fetchCountries() = firestoreProvider.getCountries().subscribeOn(Schedulers.io())

    override fun fetchAreas() = firestoreProvider.getAreas().subscribeOn(Schedulers.io())

    override fun fetchArea(name: String): Single<AreaFS> =
        firestoreProvider.getAreaByName(name).subscribeOn(Schedulers.io())
}