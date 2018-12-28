package com.riningan.wowmount.data.storage.local

import com.riningan.wowmount.data.model.Character
import com.riningan.wowmount.data.model.Mount
import io.reactivex.Completable
import io.reactivex.Single
import io.realm.Realm


class CharacterLocalStorage : BaseLocalStorage<Pair<Character?, List<Mount>>>() {
    override fun get(): Single<Pair<Character?, List<Mount>>> = Single
            .fromCallable {
                Realm.getDefaultInstance().run {
                    val character = where(Character::class.java).findFirst()
                    val mounts = where(Mount::class.java).findAll().toList()
                    close()
                    Pair(character, mounts)
                }
            }

    override fun set(cache: Pair<Character?, List<Mount>>): Completable = Completable
            .fromCallable {
                Realm.getDefaultInstance().apply {
                    beginTransaction()
                    delete(Character::class.java)
                    copyToRealm(cache.first!!)
                    delete(Mount::class.java)
                    cache.second.forEach { mount -> copyToRealm(mount) }
                    commitTransaction()
                    close()
                }
            }

    override fun clear(): Completable = Completable
            .fromCallable {
                Realm.getDefaultInstance().apply {
                    beginTransaction()
                    delete(Character::class.java)
                    delete(Mount::class.java)
                    commitTransaction()
                    close()
                }
            }
}