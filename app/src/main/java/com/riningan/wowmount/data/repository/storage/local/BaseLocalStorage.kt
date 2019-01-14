package com.riningan.wowmount.data.repository.storage.local

import io.reactivex.Completable
import io.reactivex.Single


interface BaseLocalStorage<T> {
    fun get(): Single<T>

    fun set(cache: T): Completable

    fun clear(): Completable
}