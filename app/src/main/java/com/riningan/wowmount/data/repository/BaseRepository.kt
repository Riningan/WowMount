package com.riningan.wowmount.data.repository

import java.util.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single


abstract class BaseRepository<T> {
    private var mLastUpdateTime = Date(1)
    private var mCache: T? = null


    private val updateIntervalInSeconds: Int
        get() = 4


    fun get(): Flowable<T> {
        val local = if (mCache == null) {
            getFromLocalDataSource().doOnSuccess { mCache = it }
        } else {
            getFromMemoryDataSource()
        }.toFlowable()
        return if (cacheIsDirty()) {
            val remote = update().toFlowable()
            Flowable
                    .mergeDelayError(local, remote)
                    .distinct { getHashKey(it) }
        } else {
            local
        }
    }

    fun update(): Single<T> =
            getFromRemoteDataSource()
                    .doOnSuccess {
                        mCache = it
                        mLastUpdateTime = Date()
                    }
                    .flatMap { setToLocalDataSource(it).toSingle { it } }

    fun clear(): Completable = clearLocalDataSource()
            .doOnComplete {
                mLastUpdateTime = Date(1)
                mCache = null
            }


    private fun getFromMemoryDataSource(): Single<T> = Single.just(mCache!!)

    protected abstract fun getFromLocalDataSource(): Single<T>

    protected abstract fun getFromRemoteDataSource(): Single<T>


    protected abstract fun setToLocalDataSource(cache: T): Completable


    protected abstract fun clearLocalDataSource(): Completable


    protected abstract fun getHashKey(t: T): Any


    private fun cacheIsDirty() = Date().time - mLastUpdateTime.time > updateIntervalInSeconds * 1000
}