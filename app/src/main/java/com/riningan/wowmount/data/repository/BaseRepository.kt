package com.riningan.wowmount.data.repository

import java.util.*
import io.reactivex.Observable


abstract class BaseRepository<T> {
    private var mLastUpdateTime = Date(1)
    private var mCache: T? = null


    private val updateIntervalInSeconds: Int
        get() = 60


    fun get(): Observable<T> =
            if (mCache != null && !cacheIsDirty()) {
                Observable.just(mCache!!)
            } else if (cacheIsDirty()) {
                getFromRemoteDataSource()
                        .setToMemoryCache()
                        .doOnNext { mLastUpdateTime = Date() }
                        .flatMap({ setToLocalDataSource(it) }, { it, _ -> it })
                        .onErrorResumeNext { throwable: Throwable ->
                            val localObservable = mCache?.let { Observable.just(it) }
                                    ?: loadFromLocalDataSource()
                            Observable.mergeDelayError(localObservable, Observable.error(throwable))
                        }
            } else {
                loadFromLocalDataSource()
            }

    fun clear(): Observable<Boolean> = clearLocalDataSource()
            .doOnNext {
                mLastUpdateTime = Date(1)
                mCache = null
            }


    protected abstract fun getFromRemoteDataSource(): Observable<T>

    protected abstract fun getFromLocalDataSource(): Observable<T>

    protected abstract fun setToLocalDataSource(cache: T): Observable<Boolean>

    protected abstract fun clearLocalDataSource(): Observable<Boolean>


    private fun cacheIsDirty() = Date().time - mLastUpdateTime.time > updateIntervalInSeconds * 1000

    private fun loadFromLocalDataSource() = getFromLocalDataSource()
            .setToMemoryCache()


    private fun Observable<T>.setToMemoryCache(): Observable<T> = doOnNext { mCache = it }
}