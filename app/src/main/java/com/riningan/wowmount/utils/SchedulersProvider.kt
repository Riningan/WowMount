package com.riningan.wowmount.utils

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class SchedulersProvider {
    fun executorThread(): Scheduler = Schedulers.io()
    fun postExecutionThread(): Scheduler = AndroidSchedulers.mainThread()
}