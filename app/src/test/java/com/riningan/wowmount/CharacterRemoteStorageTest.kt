package com.riningan.wowmount

import com.riningan.wowmount.data.network.BlizzardApi
import com.riningan.wowmount.data.network.SpreadsheetApi
import com.riningan.wowmount.data.preferences.LocalPreferences
import com.riningan.wowmount.data.repository.storage.remote.CharacterRemoteStorage
import com.riningan.wowmount.rule.CharacterResponseRule
import com.riningan.wowmount.rule.MountsResponseRule
import com.riningan.wowmount.rule.SpreadsheetRowsResponseRule
import com.riningan.wowmount.utils.isContain
import io.reactivex.Single
import org.junit.Before
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


class CharacterRemoteStorageTest {
    companion object {
        @ClassRule
        @JvmField
        val mMountsResponseRule = MountsResponseRule()
        @ClassRule
        @JvmField
        val mCharacterResponseRule = CharacterResponseRule()
        @ClassRule
        @JvmField
        val mSpreadsheetRowsResponseRule = SpreadsheetRowsResponseRule()

        private val mBlizzardApi = Mockito.mock(BlizzardApi::class.java)
        private val mSpreadsheetApi = Mockito.mock(SpreadsheetApi::class.java)
        private val mLocalPreferences = Mockito.mock(LocalPreferences::class.java)


        @BeforeClass
        @JvmStatic
        fun setUpClass() {
            Mockito.`when`(mLocalPreferences.server).thenReturn("TestServer")
            Mockito.`when`(mLocalPreferences.accessToken).thenReturn("TestAccessToken")
            Mockito.`when`(mLocalPreferences.characterName).thenReturn("TestCharacterName")
            Mockito.`when`(mLocalPreferences.isActivated).thenReturn(true)
            Mockito.`when`(mLocalPreferences.realmName).thenReturn("TestRealmName")

            Mockito.`when`(mBlizzardApi.getMounts(mLocalPreferences.server))
                    .thenReturn(Single.fromCallable { mMountsResponseRule.getResponse() })

            Mockito.`when`(mBlizzardApi.getCharacter(mLocalPreferences.server, mLocalPreferences.realmName, mLocalPreferences.characterName))
                    .thenReturn(Single.fromCallable { mCharacterResponseRule.getResponse() })

            Mockito.`when`(mSpreadsheetApi.getSpreadsheetRows())
                    .thenReturn(Single.fromCallable { mSpreadsheetRowsResponseRule.getResponse() })
        }
    }


    private lateinit var mCharacterRemoteStorage: CharacterRemoteStorage


    @Before
    fun setUp() {
        mCharacterRemoteStorage = CharacterRemoteStorage(mBlizzardApi, mSpreadsheetApi, mLocalPreferences)
    }


    @Test
    fun get() {
        System.out.println("Start Test get()")
        val observer = mCharacterRemoteStorage.get()
                .toObservable()
                .test()
        observer.assertValue {
            if (it.first == null) {
                System.out.println("Null character")
                false
            } else if (it.first!!.name != CharacterResponseRule.NAME ||
                    it.first!!.realm != CharacterResponseRule.REALM ||
                    it.first!!.level != CharacterResponseRule.LEVEL ||
                    it.first!!.thumbnail != CharacterResponseRule.THUMBNAIL ||
                    it.first!!.region != mLocalPreferences.server) {
                System.out.println("Wrong character")
                false
            } else if (it.second.size != MountsResponseRule.MOUNT_LIST.size) {
                System.out.println("Wrong mount list size")
                false
            } else if (it.second[0].let { mount ->
                        mount.id != CharacterRemoteStorage.createId(MountsResponseRule.MOUNT_1) ||
                                mount.name != MountsResponseRule.MOUNT_1.name ||
                                mount.itemId != MountsResponseRule.MOUNT_1.itemId ||
                                mount.qualityId != MountsResponseRule.MOUNT_1.qualityId ||
                                mount.clientId != SpreadsheetRowsResponseRule.ROW_1.clientId ||
                                mount.icon != MountsResponseRule.MOUNT_1.icon ||
                                mount.isGround != MountsResponseRule.MOUNT_1.isGround ||
                                mount.isFlying != MountsResponseRule.MOUNT_1.isFlying ||
                                mount.isAquatic != MountsResponseRule.MOUNT_1.isAquatic ||
                                mount.isCollected != CharacterResponseRule.COLLECTED_MOUNT_LIST.isContain { it.itemId == mount.itemId }
                    }) {
                System.out.println("Wrong mount 0")
                false
            } else if (it.second[1].let { mount ->
                        mount.id != CharacterRemoteStorage.createId(MountsResponseRule.MOUNT_2) ||
                                mount.name != MountsResponseRule.MOUNT_2.name ||
                                mount.itemId != MountsResponseRule.MOUNT_2.itemId ||
                                mount.qualityId != MountsResponseRule.MOUNT_2.qualityId ||
                                mount.clientId != SpreadsheetRowsResponseRule.ROW_2.clientId ||
                                mount.icon != MountsResponseRule.MOUNT_2.icon ||
                                mount.isGround != MountsResponseRule.MOUNT_2.isGround ||
                                mount.isFlying != MountsResponseRule.MOUNT_2.isFlying ||
                                mount.isAquatic != MountsResponseRule.MOUNT_2.isAquatic ||
                                mount.isCollected != CharacterResponseRule.COLLECTED_MOUNT_LIST.isContain { it.itemId == mount.itemId }
                    }) {
                System.out.println("Wrong mount 1")
                false
            } else if (it.second[2].let { mount ->
                        mount.id != CharacterRemoteStorage.createId(MountsResponseRule.MOUNT_3) ||
                                mount.name != MountsResponseRule.MOUNT_3.name ||
                                mount.itemId != MountsResponseRule.MOUNT_3.itemId ||
                                mount.qualityId != MountsResponseRule.MOUNT_3.qualityId ||
                                mount.clientId != SpreadsheetRowsResponseRule.ROW_3.clientId ||
                                mount.icon != MountsResponseRule.MOUNT_3.icon ||
                                mount.isGround != MountsResponseRule.MOUNT_3.isGround ||
                                mount.isFlying != MountsResponseRule.MOUNT_3.isFlying ||
                                mount.isAquatic != MountsResponseRule.MOUNT_3.isAquatic ||
                                mount.isCollected != CharacterResponseRule.COLLECTED_MOUNT_LIST.isContain { it.itemId == mount.itemId }
                    }) {
                System.out.println("Wrong mount 2")
                false
            } else if (it.second[3].let { mount ->
                        mount.id != CharacterRemoteStorage.createId(MountsResponseRule.MOUNT_4) ||
                                mount.name != MountsResponseRule.MOUNT_4.name ||
                                mount.itemId != MountsResponseRule.MOUNT_4.itemId ||
                                mount.qualityId != MountsResponseRule.MOUNT_4.qualityId ||
                                mount.clientId != 0 ||
                                mount.icon != MountsResponseRule.MOUNT_4.icon ||
                                mount.isGround != MountsResponseRule.MOUNT_4.isGround ||
                                mount.isFlying != MountsResponseRule.MOUNT_4.isFlying ||
                                mount.isAquatic != MountsResponseRule.MOUNT_4.isAquatic ||
                                mount.isCollected != CharacterResponseRule.COLLECTED_MOUNT_LIST.isContain { it.itemId == mount.itemId }
                    }) {
                System.out.println("Wrong mount 3")
                false
            } else {
                System.out.println("Success")
                true
            }
        }
    }
}