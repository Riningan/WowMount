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
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Test
import org.mockito.Mockito


class CharacterRemoteStorageTest {
    companion object {
        @JvmField
        @ClassRule
        val mMountsResponseRule = MountsResponseRule()
        @JvmField
        @ClassRule
        val mCharacterResponseRule = CharacterResponseRule()
        @JvmField
        @ClassRule
        val mSpreadsheetRowsResponseRule = SpreadsheetRowsResponseRule()

        private val mBlizzardApi = Mockito.mock(BlizzardApi::class.java)
        private val mSpreadsheetApi = Mockito.mock(SpreadsheetApi::class.java)
        private val mLocalPreferences = Mockito.mock(LocalPreferences::class.java)


        @JvmStatic
        @BeforeClass
        fun beforeClass() {
            System.out.println("Start test class - ${Thread.currentThread().stackTrace[1].fileName}")
            Mockito.`when`(mLocalPreferences.server).thenReturn("en")
            Mockito.`when`(mLocalPreferences.accessToken).thenReturn("Token")
            Mockito.`when`(mLocalPreferences.characterName).thenReturn(CharacterResponseRule.NAME)
            Mockito.`when`(mLocalPreferences.isActivated).thenReturn(true)
            Mockito.`when`(mLocalPreferences.realmName).thenReturn(CharacterResponseRule.REALM)

            Mockito.`when`(mBlizzardApi.getMounts(mLocalPreferences.server))
                    .thenReturn(Single.fromCallable { mMountsResponseRule.getResponse() })
            Mockito.`when`(mBlizzardApi.getCharacter(mLocalPreferences.server, mLocalPreferences.realmName, mLocalPreferences.characterName))
                    .thenReturn(Single.fromCallable { mCharacterResponseRule.getResponse() })
            Mockito.`when`(mSpreadsheetApi.getSpreadsheetRows())
                    .thenReturn(Single.fromCallable { mSpreadsheetRowsResponseRule.getResponse() })
        }

        @JvmStatic
        @AfterClass
        fun afterClass() {
            System.out.println("Finish test class - ${Thread.currentThread().stackTrace[1].fileName}")
        }
    }


    private lateinit var mCharacterRemoteStorage: CharacterRemoteStorage


    @Before
    fun before() {
        mCharacterRemoteStorage = CharacterRemoteStorage(mBlizzardApi, mSpreadsheetApi, mLocalPreferences)
    }


    @Test
    fun get() {
        System.out.println("Start test - ${Thread.currentThread().stackTrace[1].methodName}")
        val observer = mCharacterRemoteStorage.get()
                .toObservable()
                .test()

        System.out.print("Check character is not null - ")
        observer.assertValue { it.first != null }
        System.out.println("Success")

        System.out.print("Check character - ")
        observer.assertValue { (character, _) ->
            character!!.name == CharacterResponseRule.NAME &&
                    character.realm == CharacterResponseRule.REALM &&
                    character.level == CharacterResponseRule.LEVEL &&
                    character.thumbnail == CharacterResponseRule.THUMBNAIL &&
                    character.region == mLocalPreferences.server
        }
        System.out.println("Success")

        System.out.print("Check mount list size - ")
        observer.assertValue { it.second.size == MountsResponseRule.MOUNT_LIST.size }
        System.out.println("Success")

        System.out.print("Check mount 0 - ")
        observer.assertValue { (_, mounts) ->
            mounts[0].run {
                id == CharacterRemoteStorage.createId(MountsResponseRule.MOUNT_1) &&
                        name == MountsResponseRule.MOUNT_1.name &&
                        itemId == MountsResponseRule.MOUNT_1.itemId &&
                        qualityId == MountsResponseRule.MOUNT_1.qualityId &&
                        clientId == SpreadsheetRowsResponseRule.ROW_1.clientId &&
                        icon == MountsResponseRule.MOUNT_1.icon &&
                        isGround == MountsResponseRule.MOUNT_1.isGround &&
                        isFlying == MountsResponseRule.MOUNT_1.isFlying &&
                        isAquatic == MountsResponseRule.MOUNT_1.isAquatic &&
                        isCollected == CharacterResponseRule.COLLECTED_MOUNT_LIST.isContain { it.itemId == itemId }
            }
        }
        System.out.println("Success")

        System.out.print("Check mount 1 - ")
        observer.assertValue { (_, mounts) ->
            mounts[1].run {
                id == CharacterRemoteStorage.createId(MountsResponseRule.MOUNT_2) &&
                        name == MountsResponseRule.MOUNT_2.name &&
                        itemId == MountsResponseRule.MOUNT_2.itemId &&
                        qualityId == MountsResponseRule.MOUNT_2.qualityId &&
                        clientId == SpreadsheetRowsResponseRule.ROW_2.clientId &&
                        icon == MountsResponseRule.MOUNT_2.icon &&
                        isGround == MountsResponseRule.MOUNT_2.isGround &&
                        isFlying == MountsResponseRule.MOUNT_2.isFlying &&
                        isAquatic == MountsResponseRule.MOUNT_2.isAquatic &&
                        isCollected == CharacterResponseRule.COLLECTED_MOUNT_LIST.isContain { it.itemId == itemId }
            }
        }
        System.out.println("Success")

        System.out.print("Check mount 2 - ")
        observer.assertValue { (_, mounts) ->
            mounts[2].run {
                id == CharacterRemoteStorage.createId(MountsResponseRule.MOUNT_3) &&
                        name == MountsResponseRule.MOUNT_3.name &&
                        itemId == MountsResponseRule.MOUNT_3.itemId &&
                        qualityId == MountsResponseRule.MOUNT_3.qualityId &&
                        clientId == SpreadsheetRowsResponseRule.ROW_3.clientId &&
                        icon == MountsResponseRule.MOUNT_3.icon &&
                        isGround == MountsResponseRule.MOUNT_3.isGround &&
                        isFlying == MountsResponseRule.MOUNT_3.isFlying &&
                        isAquatic == MountsResponseRule.MOUNT_3.isAquatic &&
                        isCollected == CharacterResponseRule.COLLECTED_MOUNT_LIST.isContain { it.itemId == itemId }
            }
        }
        System.out.println("Success")

        System.out.print("Check mount 3 - ")
        observer.assertValue { (_, mounts) ->
            mounts[3].run {
                id == CharacterRemoteStorage.createId(MountsResponseRule.MOUNT_4) &&
                        name == MountsResponseRule.MOUNT_4.name &&
                        itemId == MountsResponseRule.MOUNT_4.itemId &&
                        qualityId == MountsResponseRule.MOUNT_4.qualityId &&
                        clientId == 0 &&
                        icon == MountsResponseRule.MOUNT_4.icon &&
                        isGround == MountsResponseRule.MOUNT_4.isGround &&
                        isFlying == MountsResponseRule.MOUNT_4.isFlying &&
                        isAquatic == MountsResponseRule.MOUNT_4.isAquatic &&
                        isCollected == CharacterResponseRule.COLLECTED_MOUNT_LIST.isContain { it.itemId == itemId }
            }
        }
        System.out.println("Success")

        System.out.println("Finish test - ${Thread.currentThread().stackTrace[1].methodName}")
    }
}