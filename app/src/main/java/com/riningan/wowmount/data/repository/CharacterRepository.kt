package com.riningan.wowmount.data.repository

import com.riningan.wowmount.data.LocalPreferences
import com.riningan.wowmount.data.model.Character
import com.riningan.wowmount.data.model.Mount
import com.riningan.wowmount.network.BlizzardApi
import com.riningan.wowmount.utils.isContain
import io.reactivex.Observable
import io.realm.Realm


class CharacterRepository constructor(private val mBlizzardApi: BlizzardApi,
                                      private val mLocalPreferences: LocalPreferences) : BaseRepository<Pair<Character, List<Mount>>>() {
    override fun getFromLocalDataSource(): Observable<Pair<Character, List<Mount>>> = Observable
            .just(Realm.getDefaultInstance())
            .map { realm ->
                val character = realm.where(Character::class.java).findFirst()!!
                val mounts = realm.where(Mount::class.java).findAll()
                realm.close()
                Pair(character, mounts.toList())
            }

    override fun setToLocalDataSource(cache: Pair<Character, List<Mount>>): Observable<Boolean> = Observable
            .just(Realm.getDefaultInstance())
            .map {
                it.apply {
                    beginTransaction()
                    delete(Character::class.java)
                    copyToRealm(cache.first)
                    delete(Mount::class.java)
                    cache.second.forEach { mount -> copyToRealm(mount) }
                    commitTransaction()
                    close()
                }
                true
            }

    override fun getFromRemoteDataSource(): Observable<Pair<Character, List<Mount>>> = mBlizzardApi
            .getMounts(mLocalPreferences.server, "en_US")
            .flatMapIterable { it }
            .map {
                Mount().apply {
                    itemId = it.itemId
                    name = it.name
                    qualityId = it.qualityId
                    icon = it.icon
                    isGround = it.isGround
                    isFlying = it.isFlying
                    isAquatic = it.isAquatic
                }
            }
            .toList()
            .toObservable()
            .flatMap({ _ ->
                mBlizzardApi.getCharacter(mLocalPreferences.server, mLocalPreferences.realmName, mLocalPreferences.characterName, "mounts", "en_US")
            }, { mounts, characterResponce ->
                val character = Character().apply {
                    name = characterResponce.name
                    realm = characterResponce.realm
                    level = characterResponce.level
                    thumbnail = characterResponce.thumbnail
                }
                mounts.forEach { mount ->
                    mount.isCollected = characterResponce.mounts.collected.isContain { it.itemId == mount.itemId }
                }
                Pair(character, mounts)
            })

    override fun clearLocalDataSource(): Observable<Boolean> = Observable
            .just(Realm.getDefaultInstance())
            .map {
                it.apply {
                    beginTransaction()
                    delete(Character::class.java)
                    delete(Mount::class.java)
                    commitTransaction()
                    close()
                }
                true
            }
}