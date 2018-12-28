package com.riningan.wowmount.data.storage.local

import io.reactivex.Completable
import io.reactivex.Single


abstract class BaseLocalStorage<T> {
    abstract fun get(): Single<T>

    abstract fun set(cache: T): Completable

    abstract fun clear(): Completable
}