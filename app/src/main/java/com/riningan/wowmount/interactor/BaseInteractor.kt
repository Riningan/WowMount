package com.riningan.wowmount.interactor

import com.riningan.util.Logger
import com.riningan.wowmount.utils.DeviceUtil
import com.riningan.wowmount.utils.SchedulersProvider
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.exceptions.CompositeException
import retrofit2.HttpException
import java.io.IOException


abstract class BaseInteractor(private val mSchedulersProvider: SchedulersProvider) {
    protected fun <T> Observable<T>.execution(): Observable<T> = this
            .subscribeOn(mSchedulersProvider.executorThread())
            .observeOn(mSchedulersProvider.postExecutionThread(), true)

    protected fun Completable.execution(): Completable = this
            .subscribeOn(mSchedulersProvider.executorThread())
            .observeOn(mSchedulersProvider.postExecutionThread())
            .doOnError {
                Logger.forThis(this@BaseInteractor).error(it)
            }

    protected fun <T> Observable<T>.errorCast(): Observable<T> = this
            .onErrorResumeNext { it: Throwable ->
                val throwable = if (it is CompositeException) {
                    it.exceptions[0]
                } else {
                    it
                }
                Logger.forThis(this@BaseInteractor).error(it)
                Observable.error(when {
                    throwable is HttpException && throwable.code() == 401 -> WowMountExceptions.AuthorizedException()
                    throwable is IOException && DeviceUtil.isOnline() -> WowMountExceptions.IOException()
                    throwable is IOException -> WowMountExceptions.NoInternetException()
                    else -> WowMountExceptions.ApplicationException()
                })
            }
}


