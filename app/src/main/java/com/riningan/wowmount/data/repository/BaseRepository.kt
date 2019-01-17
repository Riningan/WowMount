package com.riningan.wowmount.data.repository

import com.riningan.wowmount.data.repository.storage.local.BaseLocalStorage
import com.riningan.wowmount.data.repository.storage.remote.BaseRemoteStorage
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*


abstract class BaseRepository<T>(private val mLocalStorage: BaseLocalStorage<T>, private val mRemoteStorage: BaseRemoteStorage<T>) {
    private var mLastUpdateTime = Date(1)
    private var mCache: T? = null


    private val updateIntervalInSeconds: Int
        get() = 60


    fun update(): Single<T> = mRemoteStorage
            .get()
            .doOnSuccess {
                mCache = it
                mLastUpdateTime = Date()
            }
            .flatMap { mLocalStorage.set(it).toSingle { it } }

    fun get(): Flowable<T> {
        val local = if (mCache == null) {
            mLocalStorage.get().doOnSuccess { mCache = it }
        } else {
            Single.just(mCache!!)
        }.toFlowable()
        return if (cacheIsDirty()) {
            Flowable.mergeDelayError(local, update().toFlowable())
                    .distinct { calculateHashCode(it) }
        } else {
            local
        }
    }

    fun clear(): Completable = mLocalStorage
            .clear()
            .doOnComplete {
                mLastUpdateTime = Date(1)
                mCache = null
            }


    protected abstract fun calculateHashCode(t: T): Any


    private fun cacheIsDirty() = Date().time - mLastUpdateTime.time > updateIntervalInSeconds * 1000
}