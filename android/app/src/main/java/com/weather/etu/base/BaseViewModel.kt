package com.weather.etu.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

abstract class BaseViewModel: ViewModel() {
    protected val disposable = CompositeDisposable()

    val errorLivaData = MutableLiveData<Throwable>()

    protected fun <T> Single<T>.safeSubscribe(onSuccess: (T) -> Unit) =
        subscribeOn(Schedulers.io()).subscribe({ onSuccess(it, onSuccess) }, ::onErrorHandled)

    protected fun <T> Maybe<T>.safeSubscribe(onSuccess: (T) -> Unit) =
        subscribeOn(Schedulers.io()).subscribe({ onSuccess(it, onSuccess) }, ::onErrorHandled)

    protected fun Completable.safeSubscribe(onSuccess: () -> Unit) =
        subscribeOn(Schedulers.io()).subscribe({ onSuccessCompletable(onSuccess) }, ::onErrorHandled)

    protected fun <T> Observable<T>.safeSubscribe(onSuccess: (T) -> Unit) =
        subscribeOn(Schedulers.io()).subscribe({ onSuccess(it, onSuccess) }, ::onErrorHandled)

    private fun onErrorHandled(t: Throwable) {
        t.printStackTrace()
        errorLivaData.postValue(t)
    }

    private fun <T> onSuccess(value: T, action: (T) -> Unit) {
        Log.d("onSuccess", value.toString())
        action.invoke(value)
    }

    private fun onSuccessCompletable(action: () -> Unit) {
        Log.d("onSuccess", "completable")
        action.invoke()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}