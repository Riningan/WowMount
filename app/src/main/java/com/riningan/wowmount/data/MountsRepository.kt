package com.riningan.wowmount.data

import com.riningan.wowmount.data.model.Mount
import com.riningan.wowmount.network.BlizzardApi
import io.reactivex.Observable
import io.realm.Realm


class MountsRepository constructor(private val mBlizzardApi: BlizzardApi) {
    private val mMounts = ArrayList<Mount>()


    fun load(): Observable<ArrayList<Mount>> {
        val realm = Realm.getDefaultInstance()
        return realm.where(Mount::class.java)
                .findAll()
                .asChangesetObservable()
                .map {
                    mMounts.clear()
                    mMounts.addAll(it.collection)
                    realm.close()
                    mMounts
                }
                .doOnError {
                    realm.close()
                }
    }

    fun update(server: String, realmName: String, characterName: String): Observable<ArrayList<Mount>> = mBlizzardApi
            .getMounts(server, realmName, characterName, "mounts", "en_US")
            .flatMapIterable { it }
            .map { Mount(it) }
            .toList()
            .toObservable()
            .map { mounts ->
                val realm = Realm.getDefaultInstance()
                realm.beginTransaction()
                mMounts.forEach { it.deleteFromRealm() }
                mMounts.clear()
                mMounts.addAll(mounts)
                mMounts.forEach { realm.copyToRealm(it) }
                realm.commitTransaction()
                realm.close()
                mMounts
            }

    fun getMounts() = ArrayList<Mount>(mMounts)
}