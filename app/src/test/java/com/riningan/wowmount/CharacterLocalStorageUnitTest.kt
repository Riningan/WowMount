package com.riningan.wowmount

import com.riningan.wowmount.data.repository.storage.local.CharacterLocalStorage
import com.riningan.wowmount.rule.RealmDBRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(manifest = "AndroidManifest.xml", sdk = [21])
class CharacterLocalStorageUnitTest {
    @get:Rule
    val mRealmDBRule = RealmDBRule(RuntimeEnvironment.application.applicationContext)

    private lateinit var mCharacterLocalStorage: CharacterLocalStorage


    @Before
    fun before() {
        mCharacterLocalStorage = CharacterLocalStorage(mRealmDBRule.getRealm())
    }

    @Test
    fun get() {
        System.out.println("Start test - ${Thread.currentThread().stackTrace[1].methodName}")
        val observer = mCharacterLocalStorage.get()
                .toObservable()
                .test()

        System.out.print("Check character is not null - ")
        observer.assertValue { it.first != null }
        System.out.println("Success")

        System.out.print("Check character - ")
        observer.assertValue {
            it.first!!.name == RealmDBRule.CHARACTER_ENTITY.name &&
                    it.first!!.realm == RealmDBRule.CHARACTER_ENTITY.realm &&
                    it.first!!.level == RealmDBRule.CHARACTER_ENTITY.level &&
                    it.first!!.thumbnail == RealmDBRule.CHARACTER_ENTITY.thumbnail &&
                    it.first!!.region == RealmDBRule.CHARACTER_ENTITY.region
        }
        System.out.println("Success")

        System.out.print("Check mount list size - ")
        observer.assertValue { it.second.size == RealmDBRule.MOUNT_ENTITY_LIST.size }
        System.out.println("Success")

        System.out.print("Check mount 0 - ")
        observer.assertValue {
            it.second[0].run {
                id == RealmDBRule.MOUNT_ENTITY_1.id &&
                        name == RealmDBRule.MOUNT_ENTITY_1.name &&
                        itemId == RealmDBRule.MOUNT_ENTITY_1.itemId &&
                        qualityId == RealmDBRule.MOUNT_ENTITY_1.qualityId &&
                        clientId == RealmDBRule.MOUNT_ENTITY_1.clientId &&
                        icon == RealmDBRule.MOUNT_ENTITY_1.icon &&
                        isGround == RealmDBRule.MOUNT_ENTITY_1.isGround &&
                        isFlying == RealmDBRule.MOUNT_ENTITY_1.isFlying &&
                        isAquatic == RealmDBRule.MOUNT_ENTITY_1.isAquatic &&
                        isCollected == RealmDBRule.MOUNT_ENTITY_1.isCollected
            }
        }
        System.out.println("Success")

        System.out.print("Check mount 1 - ")
        observer.assertValue {
            it.second[1].run {
                id == RealmDBRule.MOUNT_ENTITY_2.id &&
                        name == RealmDBRule.MOUNT_ENTITY_2.name &&
                        itemId == RealmDBRule.MOUNT_ENTITY_2.itemId &&
                        qualityId == RealmDBRule.MOUNT_ENTITY_2.qualityId &&
                        clientId == RealmDBRule.MOUNT_ENTITY_2.clientId &&
                        icon == RealmDBRule.MOUNT_ENTITY_2.icon &&
                        isGround == RealmDBRule.MOUNT_ENTITY_2.isGround &&
                        isFlying == RealmDBRule.MOUNT_ENTITY_2.isFlying &&
                        isAquatic == RealmDBRule.MOUNT_ENTITY_2.isAquatic &&
                        isCollected == RealmDBRule.MOUNT_ENTITY_2.isCollected
            }
        }
        System.out.println("Success")

        System.out.print("Check mount 2 - ")
        observer.assertValue {
            it.second[2].run {
                id == RealmDBRule.MOUNT_ENTITY_3.id &&
                        name == RealmDBRule.MOUNT_ENTITY_3.name &&
                        itemId == RealmDBRule.MOUNT_ENTITY_3.itemId &&
                        qualityId == RealmDBRule.MOUNT_ENTITY_3.qualityId &&
                        clientId == RealmDBRule.MOUNT_ENTITY_3.clientId &&
                        icon == RealmDBRule.MOUNT_ENTITY_3.icon &&
                        isGround == RealmDBRule.MOUNT_ENTITY_3.isGround &&
                        isFlying == RealmDBRule.MOUNT_ENTITY_3.isFlying &&
                        isAquatic == RealmDBRule.MOUNT_ENTITY_3.isAquatic &&
                        isCollected == RealmDBRule.MOUNT_ENTITY_3.isCollected
            }
        }
        System.out.println("Success")

        System.out.print("Check mount 3 - ")
        observer.assertValue {
            it.second[3].run {
                id == RealmDBRule.MOUNT_ENTITY_4.id &&
                        name == RealmDBRule.MOUNT_ENTITY_4.name &&
                        itemId == RealmDBRule.MOUNT_ENTITY_4.itemId &&
                        qualityId == RealmDBRule.MOUNT_ENTITY_4.qualityId &&
                        clientId == RealmDBRule.MOUNT_ENTITY_4.clientId &&
                        icon == RealmDBRule.MOUNT_ENTITY_4.icon &&
                        isGround == RealmDBRule.MOUNT_ENTITY_4.isGround &&
                        isFlying == RealmDBRule.MOUNT_ENTITY_4.isFlying &&
                        isAquatic == RealmDBRule.MOUNT_ENTITY_4.isAquatic &&
                        isCollected == RealmDBRule.MOUNT_ENTITY_4.isCollected
            }
        }
        System.out.println("Success")

        System.out.println("Finish test - ${Thread.currentThread().stackTrace[1].methodName}")
    }

    @Test
    fun clear() {
        System.out.println("Start test - ${Thread.currentThread().stackTrace[1].methodName}")
        mCharacterLocalStorage.clear()
                .blockingAwait()
        val observer = mCharacterLocalStorage.get().toObservable()
                .test()

        System.out.print("Check empty db - ")
        observer.assertValue(Pair(null, listOf()))
        System.out.println("Success")

        System.out.println("Finish test - ${Thread.currentThread().stackTrace[1].methodName}")
    }
}