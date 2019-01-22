package com.riningan.wowmount.data.repository.storage.remote

import com.riningan.wowmount.data.network.api.BlizzardApi
import com.riningan.wowmount.data.network.api.SpreadsheetApi
import com.riningan.wowmount.data.network.model.CharacterResponse
import com.riningan.wowmount.data.network.model.MountResponse
import com.riningan.wowmount.data.network.model.MountsResponse
import com.riningan.wowmount.data.network.model.SpreadsheetCSVResponse
import com.riningan.wowmount.data.preferences.LocalPreferences
import com.riningan.wowmount.data.repository.model.Character
import com.riningan.wowmount.data.repository.model.Mount
import com.riningan.wowmount.utils.isContain
import com.riningan.wowmount.utils.isNotZero
import io.reactivex.Single
import io.reactivex.functions.Function3


class CharacterRemoteStorage(private val mBlizzardApi: BlizzardApi,
                             private val mSpreadsheetApi: SpreadsheetApi,
                             private val mLocalPreferences: LocalPreferences) : BaseRemoteStorage<Pair<Character?, List<Mount>>> {
    override fun get(): Single<Pair<Character?, List<Mount>>> = Single
            .zip(mBlizzardApi.getMounts(mLocalPreferences.server),
                    mBlizzardApi.getCharacter(mLocalPreferences.server, mLocalPreferences.realmName, mLocalPreferences.characterName),
                    mSpreadsheetApi.getSpreadsheetRows(),
                    Function3<MountsResponse, CharacterResponse, List<SpreadsheetCSVResponse>, Triple<MountsResponse, CharacterResponse, List<SpreadsheetCSVResponse>>> { t1: MountsResponse, t2: CharacterResponse, t3: List<SpreadsheetCSVResponse> ->
                        Triple(t1, t2, t3)
                    })
            .map { (mountsResponse, characterResponse, spreadsheetCSVRows) ->
                val character = Character().apply {
                    name = characterResponse.name
                    realm = characterResponse.realm
                    level = characterResponse.level
                    thumbnail = characterResponse.thumbnail
                    region = mLocalPreferences.server
                }
                val mounts = mountsResponse.mounts
                        .filter { it.itemId.isNotZero() && it.qualityId.isNotZero() && it.spellId.isNotZero() }
                        .map { mountResponse ->
                            Mount(
                                    createId(mountResponse),
                                    mountResponse.name,
                                    mountResponse.itemId,
                                    mountResponse.qualityId,
                                    spreadsheetCSVRows.find {
                                        it.itemId == mountResponse.itemId && it.creatureId == mountResponse.creatureId && it.spellId == mountResponse.spellId
                                    }?.clientId ?: 0,
                                    mountResponse.icon,
                                    mountResponse.isGround,
                                    mountResponse.isFlying,
                                    mountResponse.isAquatic,
                                    characterResponse.mounts.collected.isContain { it.itemId == mountResponse.itemId })
                        }
                Pair(character, mounts)
            }


    companion object {
        fun createId(mountResponse: MountResponse) = "${mountResponse.name}/${mountResponse.itemId}/${mountResponse.qualityId}"
    }
}