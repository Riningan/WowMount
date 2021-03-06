package com.riningan.wowmount.storage

import com.riningan.wowmount.CHARACTER
import com.riningan.wowmount.CHARACTER_RESPONSE
import com.riningan.wowmount.MOUNT_1
import com.riningan.wowmount.MOUNT_2
import com.riningan.wowmount.MOUNT_3
import com.riningan.wowmount.MOUNT_4
import com.riningan.wowmount.MOUNT_RESPONSE_LIST
import com.riningan.wowmount.NAME
import com.riningan.wowmount.REALM
import com.riningan.wowmount.REGION
import com.riningan.wowmount.ROW_LIST
import com.riningan.wowmount.data.network.api.BlizzardApi
import com.riningan.wowmount.data.network.api.SpreadsheetApi
import com.riningan.wowmount.data.network.model.MountsResponse
import com.riningan.wowmount.data.preferences.LocalPreferences
import com.riningan.wowmount.data.repository.storage.remote.CharacterRemoteStorage
import com.riningan.wowmount.rule.LogRule
import io.reactivex.Single
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class CharacterRemoteStorageTest {
    companion object {
        private val mBlizzardApi = Mockito.mock(BlizzardApi::class.java)
        private val mSpreadsheetApi = Mockito.mock(SpreadsheetApi::class.java)
        private val mLocalPreferences = Mockito.mock(LocalPreferences::class.java)


        @JvmStatic
        @BeforeClass
        fun setupClass() {
            Mockito.`when`(mLocalPreferences.server).thenReturn(REGION)
            Mockito.`when`(mLocalPreferences.characterName).thenReturn(NAME)
            Mockito.`when`(mLocalPreferences.realmName).thenReturn(REALM)

            Mockito.`when`(mBlizzardApi.getMounts(mLocalPreferences.server))
                    .thenReturn(Single.fromCallable { MountsResponse(MOUNT_RESPONSE_LIST) })
            Mockito.`when`(mBlizzardApi.getCharacter(mLocalPreferences.server, mLocalPreferences.realmName, mLocalPreferences.characterName))
                    .thenReturn(Single.fromCallable { CHARACTER_RESPONSE })
            Mockito.`when`(mSpreadsheetApi.getSpreadsheetRows())
                    .thenReturn(Single.fromCallable { ROW_LIST })
        }
    }


    @get: Rule
    val mLogRule = LogRule()

    private lateinit var mCharacterRemoteStorage: CharacterRemoteStorage


    @Before
    fun setup() {
        mCharacterRemoteStorage = CharacterRemoteStorage(mBlizzardApi, mSpreadsheetApi, mLocalPreferences)
    }


    /**
     * @see CharacterRemoteStorage.get()
     */
    @Test
    fun get() {
        mCharacterRemoteStorage
                .get()
                .toObservable()
                .test()
                .assertValue { it.first != null }
                .assertValue { it.first.toString() == CHARACTER.toString() }
                .assertValue { it.second.size == MOUNT_RESPONSE_LIST.size }
                .assertValue { it.second[0].toString() == MOUNT_1.toString() }
                .assertValue { it.second[1].toString() == MOUNT_2.toString() }
                .assertValue { it.second[2].toString() == MOUNT_3.toString() }
                .assertValue { it.second[3].toString() == MOUNT_4.toString() }
    }
}