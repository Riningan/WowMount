package com.riningan.wowmount.data.repository.storage.remote

import io.reactivex.Single


abstract class BaseRemoteStorage<T> {
    abstract fun get(): Single<T>
}