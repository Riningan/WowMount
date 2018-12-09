package com.riningan.wowmount.data.repository

import com.riningan.wowmount.data.LocalPreferences
import com.riningan.wowmount.data.model.Character
import com.riningan.wowmount.data.model.Mount
import com.riningan.wowmount.network.BlizzardApi
import com.riningan.wowmount.utils.LocaleUtil
import com.riningan.wowmount.utils.isContain
import io.reactivex.Observable
import io.realm.Realm


class CharacterRepository constructor(private val mBlizzardApi: BlizzardApi,
                                      private val mLocalPreferences: LocalPreferences) : BaseRepository<Pair<Character?, List<Mount>>>() {
    override fun getFromLocalDataSource(): Observable<Pair<Character?, List<Mount>>> = Observable
            .fromCallable {
                val realm = Realm.getDefaultInstance()
                val character = realm.where(Character::class.java).findFirst()
                val mounts = realm.where(Mount::class.java).findAll().toList()
                realm.close()
                Pair(character, mounts)
            }

    override fun setToLocalDataSource(cache: Pair<Character?, List<Mount>>): Observable<Boolean> = Observable
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
                true
            }

    override fun getFromRemoteDataSource(): Observable<Pair<Character?, List<Mount>>> = mBlizzardApi
            .getMounts(mLocalPreferences.server, LocaleUtil.getLocale())
            .flatMapIterable { it.mounts }
            .map {
                Mount().apply {
                    id = it.name + "/" + it.itemId + "/" + it.qualityId
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
            .flatMap({
                mBlizzardApi.getCharacter(mLocalPreferences.server, mLocalPreferences.realmName, mLocalPreferences.characterName, "mounts", LocaleUtil.getLocale())
            }, { mounts, characterResponce ->
                val character = Character().apply {
                    name = characterResponce.name
                    realm = characterResponce.realm
                    level = characterResponce.level
                    thumbnail = characterResponce.thumbnail
                    region = mLocalPreferences.server
                }
                mounts.forEach { mount ->
                    mount.isCollected = characterResponce.mounts.collected.isContain { it.itemId == mount.itemId }
                }
                Pair(character, mounts)
            })

    override fun clearLocalDataSource(): Observable<Boolean> = Observable
            .fromCallable {
                Realm.getDefaultInstance().apply {
                    beginTransaction()
                    delete(Character::class.java)
                    delete(Mount::class.java)
                    commitTransaction()
                    close()
                }
                true
            }

    fun getMountById(mountId: String): Observable<Mount> = get()
            .map { (_, mounts) ->
                mounts.find { it.id == mountId }
                        ?: throw NullPointerException("No mount with itemId = $mountId")
            }
}