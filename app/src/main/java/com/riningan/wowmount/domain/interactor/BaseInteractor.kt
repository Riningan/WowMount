package com.riningan.wowmount.domain.interactor

import com.riningan.util.Logger
import com.riningan.wowmount.domain.SchedulersProvider
import com.riningan.wowmount.domain.WowMountExceptions.Companion.throwableCast
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single


abstract class BaseInteractor(private val mSchedulersProvider: SchedulersProvider) {
    protected fun <T> Flowable<T>.execution(): Flowable<T> = this
            .subscribeOn(mSchedulersProvider.executorThread())
            .observeOn(mSchedulersProvider.postExecutionThread(), true)

    protected fun <T> Observable<T>.execution(): Observable<T> = this
            .subscribeOn(mSchedulersProvider.executorThread())
            .observeOn(mSchedulersProvider.postExecutionThread(), true)

    protected fun <T> Single<T>.execution(): Single<T> = this
            .subscribeOn(mSchedulersProvider.executorThread())
            .observeOn(mSchedulersProvider.postExecutionThread())

    protected fun Completable.execution(): Completable = this
            .subscribeOn(mSchedulersProvider.executorThread())
            .observeOn(mSchedulersProvider.postExecutionThread())


    protected fun <T> Flowable<T>.errorCast(): Flowable<T> = this
            .onErrorResumeNext { it: Throwable ->
                Logger.forThis(this@BaseInteractor).error(it)
                Flowable.error(throwableCast(it))
            }

    protected fun <T> Observable<T>.errorCast(): Observable<T> = this
            .onErrorResumeNext { it: Throwable ->
                Logger.forThis(this@BaseInteractor).error(it)
                Observable.error(throwableCast(it))
            }

    protected fun <T> Single<T>.errorCast(): Single<T> = this
            .onErrorResumeNext { it: Throwable ->
                Logger.forThis(this@BaseInteractor).error(it)
                Single.error(throwableCast(it))
            }

    protected fun Completable.errorCast(): Completable = this
            .onErrorResumeNext { it: Throwable ->
                Logger.forThis(this@BaseInteractor).error(it)
                Completable.error(throwableCast(it))
            }
}


