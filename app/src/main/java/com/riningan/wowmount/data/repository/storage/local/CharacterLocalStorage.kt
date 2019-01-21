package com.riningan.wowmount.data.repository.storage.local

import com.riningan.wowmount.data.db.DBHelper
import com.riningan.wowmount.data.db.model.CharacterEntity
import com.riningan.wowmount.data.db.model.MountEntity
import com.riningan.wowmount.data.repository.model.Character
import com.riningan.wowmount.data.repository.model.Mount
import io.reactivex.Completable
import io.reactivex.Single


class CharacterLocalStorage(private val mDBHelper: DBHelper) : BaseLocalStorage<Pair<Character?, List<Mount>>> {
    override fun get(): Single<Pair<Character?, List<Mount>>> = Single
            .fromCallable {
                mDBHelper.getDBInstance().run {
                    val character = where(CharacterEntity::class.java)
                            .findFirst()
                            ?.run {
                                Character(
                                        name,
                                        realm,
                                        level,
                                        thumbnail,
                                        region)
                            }
                    val mounts = where(MountEntity::class.java)
                            .findAll()
                            .toList()
                            .map {
                                Mount(
                                        it.id,
                                        it.name,
                                        it.itemId,
                                        it.qualityId,
                                        it.clientId,
                                        it.icon,
                                        it.isGround,
                                        it.isFlying,
                                        it.isAquatic,
                                        it.isCollected)
                            }
                    close()
                    Pair(character, mounts)
                }
            }

    override fun set(cache: Pair<Character?, List<Mount>>): Completable = Completable
            .fromCallable {
                mDBHelper.getDBInstance().apply {
                    if (cache.first == null) {
                        throw NullPointerException("Character is null")
                    }
                    beginTransaction()
                    delete(CharacterEntity::class.java)
                    cache.first!!.let {
                        createObject(CharacterEntity::class.java).apply {
                            name = it.name
                            realm = it.realm
                            level = it.level
                            thumbnail = it.thumbnail
                            region = it.region
                        }
                    }
                    delete(MountEntity::class.java)
                    cache.second.forEach {
                        createObject(MountEntity::class.java).apply {
                            id = it.id
                            name = it.name
                            itemId = it.itemId
                            qualityId = it.qualityId
                            icon = it.icon
                            isGround = it.isGround
                            isFlying = it.isFlying
                            isAquatic = it.isAquatic
                            isCollected = it.isCollected
                        }
                    }
                    commitTransaction()
                    close()
                }
            }

    override fun clear(): Completable = Completable
            .fromCallable {
                mDBHelper.getDBInstance().apply {
                    beginTransaction()
                    delete(CharacterEntity::class.java)
                    delete(MountEntity::class.java)
                    commitTransaction()
                    close()
                }
            }
}