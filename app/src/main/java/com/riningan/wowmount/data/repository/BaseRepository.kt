package com.riningan.wowmount.data.repository

import java.util.*
import io.reactivex.Observable


abstract class BaseRepository<T> {
    private var mLastUpdateTime = Date(1)
    private var mCache: T? = null


    private val updateIntervalInSeconds: Int
        get() = 4


    fun get(): Observable<T> =
            if (mCache != null && !cacheIsDirty()) {
                getFromMemoryDataSource()
            } else if (cacheIsDirty()) {
                val local = if (mCache == null) loadFromLocalDataSource() else getFromMemoryDataSource()
                val remote = update()
                Observable
                        .mergeDelayError(local, remote)
            } else {
                // no cache
                loadFromLocalDataSource()
            }

    fun update(): Observable<T> =
            getFromRemoteDataSource()
                    .setToMemoryDataSource()
                    .doOnNext { mLastUpdateTime = Date() }
                    .flatMap({ setToLocalDataSource(it) }, { it, _ -> it })

    fun clear(): Observable<Boolean> = clearLocalDataSource()
            .doOnNext {
                mLastUpdateTime = Date(1)
                mCache = null
            }


    private fun getFromMemoryDataSource(): Observable<T> = Observable.just(mCache!!)

    protected abstract fun getFromLocalDataSource(): Observable<T>

    protected abstract fun getFromRemoteDataSource(): Observable<T>


    private fun Observable<T>.setToMemoryDataSource(): Observable<T> = doOnNext { mCache = it }

    protected abstract fun setToLocalDataSource(cache: T): Observable<Boolean>


    protected abstract fun clearLocalDataSource(): Observable<Boolean>


    private fun cacheIsDirty() = Date().time - mLastUpdateTime.time > updateIntervalInSeconds * 1000

    private fun loadFromLocalDataSource() = getFromLocalDataSource().setToMemoryDataSource()
}