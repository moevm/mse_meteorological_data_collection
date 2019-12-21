package com.weather.core.remote.providers.firestore

import android.content.SharedPreferences
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.weather.core.remote.models.firebase.AreaFS
import com.weather.core.remote.models.firebase.CountryFS
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class FirestoreProviderImpl(
    private val sharedPreferences: SharedPreferences
) : FirestoreProvider {

    companion object {
        private const val AREAS_KEY = "areas.key"
    }

    override fun getCountries(): Single<List<CountryFS>> {
        return Single.create<List<CountryFS>> { emitter ->
            FirebaseFirestore
                .getInstance()
                .collection("countries")
                .get()
                .addOnSuccessListener {
                    val lst = mutableListOf<CountryFS>()

                    for (doc in it.documents) {
                        val obj = doc.toObject(CountryFS::class.java) ?: continue
                        obj.countryName = doc.id
                        lst.add(obj)
                    }


                    emitter.onSuccess(lst.filter {
                        !it.areas.isNullOrEmpty() && !it.countryName.isNullOrEmpty()
                    })
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    override fun getAreaByName(name: String): Single<AreaFS> {
        return Single.create { emitter ->
            FirebaseFirestore
                .getInstance()
                .collection("areas")
                .document(name)
                .get()
                .addOnSuccessListener {
                    val area = it.toObject(AreaFS::class.java)
                    area?.name = it.id
                    emitter.onSuccess(area)
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    override fun getAreas(): Single<List<AreaFS>> {
        return checkAreasInSp() ?: Single.create<List<AreaFS>> { emitter ->
            FirebaseFirestore
                .getInstance()
                .collection("areas")
                .get()
                .addOnSuccessListener {
                    val lst = mutableListOf<AreaFS>()

                    for (doc in it.documents) {
                        val obj = doc.toObject(AreaFS::class.java) ?: continue
                        obj.name = doc.id
                        lst.add(obj)
                    }

                    saveAreasInSp(lst)

                    emitter.onSuccess(lst.filter {
                        !it.cities.isNullOrEmpty() && !it.name.isNullOrEmpty()
                    })
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    private fun checkAreasInSp(): Single<List<AreaFS>>? {
        return if (sharedPreferences.contains(AREAS_KEY)) {
            Single.create<List<AreaFS>> {
                val areasStr = sharedPreferences.getString(AREAS_KEY, null)
                it.onSuccess(
                    Gson().fromJson(
                        areasStr,
                        object : TypeToken<List<AreaFS>>() {}.type
                    )
                )
            }.subscribeOn(Schedulers.io())
        } else {
            null
        }
    }

    private fun saveAreasInSp(areas: List<AreaFS>) {
        val obj = Gson().toJson(areas)
        sharedPreferences.edit().putString(AREAS_KEY, obj).apply()
    }
}