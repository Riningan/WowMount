package com.riningan.wowmount.data.repository.storage.remote

import io.reactivex.Single


interface BaseRemoteStorage<T> {
    fun get(): Single<T>
}