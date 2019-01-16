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
import org.junit.*
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
        fun setupClass() {
            System.out.println("Start test class - ${Thread.currentThread().stackTrace[1].fileName}")
            Mockito.`when`(mLocalPreferences.server).thenReturn(REGION)
            Mockito.`when`(mLocalPreferences.accessToken).thenReturn("Token")
            Mockito.`when`(mLocalPreferences.characterName).thenReturn(NAME)
            Mockito.`when`(mLocalPreferences.isActivated).thenReturn(true)
            Mockito.`when`(mLocalPreferences.realmName).thenReturn(REALM)

            Mockito.`when`(mBlizzardApi.getMounts(mLocalPreferences.server))
                    .thenReturn(Single.fromCallable { mMountsResponseRule.getResponse() })
            Mockito.`when`(mBlizzardApi.getCharacter(mLocalPreferences.server, mLocalPreferences.realmName, mLocalPreferences.characterName))
                    .thenReturn(Single.fromCallable { mCharacterResponseRule.getResponse() })
            Mockito.`when`(mSpreadsheetApi.getSpreadsheetRows())
                    .thenReturn(Single.fromCallable { mSpreadsheetRowsResponseRule.getResponse() })
        }

        @JvmStatic
        @AfterClass
        fun clearClass() {
            System.out.println("Finish test class - ${Thread.currentThread().stackTrace[1].fileName}")
        }
    }


    private lateinit var mCharacterRemoteStorage: CharacterRemoteStorage


    @Before
    fun setup() {
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
            character!!.name == NAME &&
                    character.realm == REALM &&
                    character.level == LEVEL &&
                    character.thumbnail == THUMBNAIL &&
                    character.region == REGION
        }
        System.out.println("Success")

        System.out.print("Check mount list size - ")
        observer.assertValue { it.second.size == MOUNT_RESPONSE_LIST.size }
        System.out.println("Success")

        System.out.print("Check mount 0 - ")
        observer.assertValue { (_, mounts) ->
            mounts[0].run {
                id == CharacterRemoteStorage.createId(MOUNT_RESPONSE_1) &&
                        name == MOUNT_RESPONSE_1.name &&
                        itemId == MOUNT_RESPONSE_1.itemId &&
                        qualityId == MOUNT_RESPONSE_1.qualityId &&
                        clientId == ROW_1.clientId &&
                        icon == MOUNT_RESPONSE_1.icon &&
                        isGround == MOUNT_RESPONSE_1.isGround &&
                        isFlying == MOUNT_RESPONSE_1.isFlying &&
                        isAquatic == MOUNT_RESPONSE_1.isAquatic &&
                        isCollected == CHARACTER_COLLECTED_MOUNT_LIST.isContain { it.itemId == itemId }
            }
        }
        System.out.println("Success")

        System.out.print("Check mount 1 - ")
        observer.assertValue { (_, mounts) ->
            mounts[1].run {
                id == CharacterRemoteStorage.createId(MOUNT_RESPONSE_2) &&
                        name == MOUNT_RESPONSE_2.name &&
                        itemId == MOUNT_RESPONSE_2.itemId &&
                        qualityId == MOUNT_RESPONSE_2.qualityId &&
                        clientId == ROW_2.clientId &&
                        icon == MOUNT_RESPONSE_2.icon &&
                        isGround == MOUNT_RESPONSE_2.isGround &&
                        isFlying == MOUNT_RESPONSE_2.isFlying &&
                        isAquatic == MOUNT_RESPONSE_2.isAquatic &&
                        isCollected == CHARACTER_COLLECTED_MOUNT_LIST.isContain { it.itemId == itemId }
            }
        }
        System.out.println("Success")

        System.out.print("Check mount 2 - ")
        observer.assertValue { (_, mounts) ->
            mounts[2].run {
                id == CharacterRemoteStorage.createId(MOUNT_RESPONSE_3) &&
                        name == MOUNT_RESPONSE_3.name &&
                        itemId == MOUNT_RESPONSE_3.itemId &&
                        qualityId == MOUNT_RESPONSE_3.qualityId &&
                        clientId == ROW_3.clientId &&
                        icon == MOUNT_RESPONSE_3.icon &&
                        isGround == MOUNT_RESPONSE_3.isGround &&
                        isFlying == MOUNT_RESPONSE_3.isFlying &&
                        isAquatic == MOUNT_RESPONSE_3.isAquatic &&
                        isCollected == CHARACTER_COLLECTED_MOUNT_LIST.isContain { it.itemId == itemId }
            }
        }
        System.out.println("Success")

        System.out.print("Check mount 3 - ")
        observer.assertValue { (_, mounts) ->
            mounts[3].run {
                id == CharacterRemoteStorage.createId(MOUNT_RESPONSE_4) &&
                        name == MOUNT_RESPONSE_4.name &&
                        itemId == MOUNT_RESPONSE_4.itemId &&
                        qualityId == MOUNT_RESPONSE_4.qualityId &&
                        clientId == 0 &&
                        icon == MOUNT_RESPONSE_4.icon &&
                        isGround == MOUNT_RESPONSE_4.isGround &&
                        isFlying == MOUNT_RESPONSE_4.isFlying &&
                        isAquatic == MOUNT_RESPONSE_4.isAquatic &&
                        isCollected == CHARACTER_COLLECTED_MOUNT_LIST.isContain { it.itemId == itemId }
            }
        }
        System.out.println("Success")

        System.out.println("Finish test - ${Thread.currentThread().stackTrace[1].methodName}")
    }
}