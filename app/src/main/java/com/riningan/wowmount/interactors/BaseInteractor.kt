package com.riningan.wowmount.interactors

import com.riningan.wowmount.utils.LogUtil
import io.reactivex.Observable
import io.reactivex.Scheduler


abstract class BaseInteractor constructor(private val mExecutorThread: Scheduler, private val mPostExecutionThread: Scheduler) {
    protected fun <T> Observable<T>.execution(): Observable<T> = this
            .subscribeOn(mExecutorThread)
            .observeOn(mPostExecutionThread)
            .doOnError { LogUtil.addError(this@BaseInteractor, it) }
}