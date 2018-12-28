package com.riningan.wowmount.data.storage.remote

import io.reactivex.Single


abstract class BaseRemoteStorage<T> {
    abstract fun get(): Single<T>
}