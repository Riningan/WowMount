package com.riningan.wowmount

import com.riningan.wowmount.data.LocalPreferences
import com.riningan.wowmount.data.storage.remote.CharacterRemoteStorage
import com.riningan.wowmount.network.BlizzardApi
import com.riningan.wowmount.network.model.CharacterMountsResponse
import com.riningan.wowmount.network.model.CharacterResponse
import com.riningan.wowmount.network.model.MountResponse
import com.riningan.wowmount.network.model.MountsResponse
import com.riningan.wowmount.utils.LocaleUtil
import io.reactivex.Single
import org.junit.Test
import org.junit.Before
import org.junit.BeforeClass
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class CharacterRemoteStorageTest {
    @Mock
    lateinit var mBlizzardApi: BlizzardApi
    @Mock
    lateinit var mLocalPreferences: LocalPreferences
    private lateinit var mCharacterRemoteStorage: CharacterRemoteStorage


    @BeforeClass
    fun setUpClass() {
        Mockito.`when`(mLocalPreferences.server).thenReturn("TestServer")
        Mockito.`when`(mLocalPreferences.accessToken).thenReturn("TestAccessToken")
        Mockito.`when`(mLocalPreferences.characterName).thenReturn("TestCharacterName")
        Mockito.`when`(mLocalPreferences.isActivated).thenReturn(true)
        Mockito.`when`(mLocalPreferences.realmName).thenReturn("TestRealmName")

        Mockito.`when`(mBlizzardApi.getMounts(mLocalPreferences.server, LocaleUtil.getLocale()))
                .thenReturn(Single.fromCallable {
                    MountsResponse(
                            listOf(
                                    MountResponse(
                                            "Test mount 1",
                                            1,
                                            1,
                                            1,
                                            1,
                                            "Test mount 1 Icon",
                                            true,
                                            true,
                                            true,
                                            true
                                    ),
                                    MountResponse(
                                            "Test mount 2",
                                            2,
                                            2,
                                            2,
                                            2,
                                            "Test mount 2 Icon",
                                            true,
                                            true,
                                            true,
                                            true
                                    )
                            )
                    )
                })
        Mockito.`when`(mBlizzardApi.getCharacter(mLocalPreferences.server, mLocalPreferences.realmName, mLocalPreferences.characterName, "mounts", LocaleUtil.getLocale()))
                .thenReturn(Single.fromCallable {
                    CharacterResponse(
                            0L,
                            "name",
                            "realm",
                            "battlegroup",
                            0,
                            0,
                            0,
                            0,
                            0,
                            "thumbnail",
                            "calsClass",
                            0,
                            CharacterMountsResponse(
                                    0,
                                    0,
                                    listOf(
                                            MountResponse(
                                                    "Test mount 1",
                                                    1,
                                                    1,
                                                    1,
                                                    1,
                                                    "Test mount 1 Icon",
                                                    true,
                                                    true,
                                                    true,
                                                    true
                                            ),
                                            MountResponse(
                                                    "Test mount 2",
                                                    2,
                                                    2,
                                                    2,
                                                    2,
                                                    "Test mount 2 Icon",
                                                    true,
                                                    true,
                                                    true,
                                                    true
                                            )
                                    )
                            ),
                            0
                    )
                })
    }


    @Before
    fun setUp() {
        mCharacterRemoteStorage = CharacterRemoteStorage(mBlizzardApi, mLocalPreferences)
    }

    @Test
    fun get() {
        val observer = mCharacterRemoteStorage.get()
                .toObservable()
                .test()
        observer.assertValue {
            if (it.first == null) {
                false
            } else if (it.first!!.name != "name") {
                false
            } else if (it.first!!.level != 0) {
                false
            } else if (it.first!!.name != "name") {
                false
            } else if (it.first!!.name != "name") {
                false
            } else if (it.first!!.name != "name") {
                false
            } else {
                true
            }
        }
    }
}
