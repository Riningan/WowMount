package com.riningan.wowmount.data.repository

import com.riningan.wowmount.data.LocalPreferences
import com.riningan.wowmount.data.model.Character
import com.riningan.wowmount.data.model.Mount
import com.riningan.wowmount.network.BlizzardApi
import com.riningan.wowmount.network.model.CharacterResponse
import com.riningan.wowmount.network.model.MountsResponse
import com.riningan.wowmount.utils.LocaleUtil
import com.riningan.wowmount.utils.isContain
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.realm.Realm


class CharacterRepository constructor(private val mBlizzardApi: BlizzardApi,
                                      private val mLocalPreferences: LocalPreferences) : BaseRepository<Pair<Character?, List<Mount>>>() {
    override fun getFromLocalDataSource(): Single<Pair<Character?, List<Mount>>> = Single
            .fromCallable {
                val realm = Realm.getDefaultInstance()
                val character = realm.where(Character::class.java).findFirst()
                val mounts = realm.where(Mount::class.java).findAll().toList()
                realm.close()
                Pair(character, mounts)
            }

    override fun setToLocalDataSource(cache: Pair<Character?, List<Mount>>): Completable = Completable
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

    override fun getFromRemoteDataSource(): Single<Pair<Character?, List<Mount>>> = Single
            .zip(mBlizzardApi.getMounts(mLocalPreferences.server, LocaleUtil.getLocale()),
                    mBlizzardApi.getCharacter(mLocalPreferences.server, mLocalPreferences.realmName, mLocalPreferences.characterName, "mounts", LocaleUtil.getLocale()),
                    BiFunction<MountsResponse, CharacterResponse, Pair<MountsResponse, CharacterResponse>> { mounts: MountsResponse, character: CharacterResponse ->
                        Pair(mounts, character)
                    })
            .map { (mountsResponse, characterResponse) ->
                val mounts = arrayListOf<Mount>()
                for (mount in mountsResponse.mounts) {
                    mounts.add(Mount().apply {
                        id = mount.name + "/" + mount.itemId + "/" + mount.qualityId
                        itemId = mount.itemId
                        name = mount.name
                        qualityId = mount.qualityId
                        icon = mount.icon
                        isGround = mount.isGround
                        isFlying = mount.isFlying
                        isAquatic = mount.isAquatic
                    })
                }
                val character = Character().apply {
                    name = characterResponse.name
                    realm = characterResponse.realm
                    level = characterResponse.level
                    thumbnail = characterResponse.thumbnail
                    region = mLocalPreferences.server
                }
                mounts.forEach { mount ->
                    mount.isCollected = characterResponse.mounts.collected.isContain { it.itemId == mount.itemId }
                }
                Pair(character, mounts)
            }


    override fun clearLocalDataSource(): Completable = Completable
            .fromCallable {
                Realm.getDefaultInstance().apply {
                    beginTransaction()
                    delete(Character::class.java)
                    delete(Mount::class.java)
                    commitTransaction()
                    close()
                }
            }

    fun getMountById(mountId: String): Flowable<Mount> = get()
            .map { (_, mounts) ->
                mounts.find { it.id == mountId }
                        ?: throw NullPointerException("No mount with itemId = $mountId")
            }

    override fun getHashKey(t: Pair<Character?, List<Mount>>): Any {
        // todo hash
        return t.second.size
    }
}