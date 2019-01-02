package com.riningan.wowmount.data.repository.storage.remote

import com.riningan.wowmount.data.network.BlizzardApi
import com.riningan.wowmount.data.network.SpreadsheetApi
import com.riningan.wowmount.data.network.model.CharacterResponse
import com.riningan.wowmount.data.network.model.MountsResponse
import com.riningan.wowmount.data.network.model.SpreadsheetCSVResponse
import com.riningan.wowmount.data.preferences.LocalPreferences
import com.riningan.wowmount.data.repository.model.Character
import com.riningan.wowmount.data.repository.model.Mount
import com.riningan.wowmount.utils.isContain
import io.reactivex.Single
import io.reactivex.functions.Function3


class CharacterRemoteStorage(private val mBlizzardApi: BlizzardApi,
                             private val mSpreadsheetApi: SpreadsheetApi,
                             private val mLocalPreferences: LocalPreferences) : BaseRemoteStorage<Pair<Character?, List<Mount>>>() {
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
                        .map { mount ->
                            Mount(
                                    "${mount.name}/${mount.itemId}/${mount.qualityId}",
                                    mount.name,
                                    mount.itemId,
                                    mount.qualityId,
                                    spreadsheetCSVRows.find {
                                        it.itemId == mount.itemId && it.spellId == mount.spellId
                                    }?.clientId ?: 0,
                                    mount.icon,
                                    mount.isGround,
                                    mount.isFlying,
                                    mount.isAquatic,
                                    characterResponse.mounts.collected.isContain { it.itemId == mount.itemId })
                        }
                Pair(character, mounts)
            }
}