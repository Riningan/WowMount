package com.riningan.wowmount.interactors

import com.riningan.util.Logger
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler


abstract class BaseInteractor constructor(private val mExecutorThread: Scheduler, private val mPostExecutionThread: Scheduler) {
    protected fun <T> Observable<T>.execution(): Observable<T> = this
            .subscribeOn(mExecutorThread)
            .observeOn(mPostExecutionThread, true)
            .doOnError {
                Logger.forThis(this@BaseInteractor).error(it)
            }

    protected fun Completable.execution(): Completable = this
            .subscribeOn(mExecutorThread)
            .observeOn(mPostExecutionThread)
            .doOnError {
                Logger.forThis(this@BaseInteractor).error(it)
            }
}