package com.riningan.wowmount.data.storage.remote

import com.riningan.wowmount.data.LocalPreferences
import com.riningan.wowmount.data.model.Character
import com.riningan.wowmount.data.model.Mount
import com.riningan.wowmount.network.BlizzardApi
import com.riningan.wowmount.network.model.CharacterResponse
import com.riningan.wowmount.network.model.MountsResponse
import com.riningan.wowmount.utils.LocaleUtil
import com.riningan.wowmount.utils.isContain
import io.reactivex.Single
import io.reactivex.functions.BiFunction


class CharacterRemoteStorage(private val mBlizzardApi: BlizzardApi,
                             private val mLocalPreferences: LocalPreferences) : BaseRemoteStorage<Pair<Character?, List<Mount>>>() {
    override fun get(): Single<Pair<Character?, List<Mount>>> = Single
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
}