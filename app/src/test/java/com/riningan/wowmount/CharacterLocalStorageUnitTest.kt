package com.riningan.wowmount

import com.riningan.wowmount.data.db.model.CharacterEntity
import com.riningan.wowmount.data.db.model.MountEntity
import com.riningan.wowmount.data.repository.model.Character
import com.riningan.wowmount.data.repository.storage.local.CharacterLocalStorage
import io.realm.Realm
import io.realm.RealmQuery
import io.realm.RealmResults
import io.realm.log.RealmLog
import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.modules.junit4.PowerMockRunnerDelegate
import org.powermock.modules.junit4.rule.PowerMockRule
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(PowerMockRunner::class)
@PowerMockRunnerDelegate(RobolectricTestRunner::class)
@Config(manifest = "AndroidManifest.xml", sdk = [21])
@PowerMockIgnore(value = ["org.mockito.*", "org.robolectric.*", "android.*"])
@SuppressStaticInitializationFor("io.realm.internal.Util")
@PrepareForTest(value = [Realm::class, RealmLog::class, RealmResults::class, RealmQuery::class, Character::class])
class CharacterLocalStorageUnitTest {
    @get: Rule
    var mPowerMockRule = PowerMockRule()

    private lateinit var mRealm: Realm
    private lateinit var mCharacterLocalStorage: CharacterLocalStorage


    @Before
    fun setup() {
        PowerMockito.mockStatic(RealmLog::class.java)
        PowerMockito.mockStatic(Realm::class.java)
        PowerMockito.mockStatic(RealmResults::class.java)
        mRealm = PowerMockito.mock(Realm::class.java)
        PowerMockito.`when`(Realm.getDefaultInstance()).thenReturn(mRealm)
        PowerMockito.doNothing().`when`(mRealm).close()
        mCharacterLocalStorage = CharacterLocalStorage(Realm.getDefaultInstance())
    }


    @Test
    fun checkRealmMock() {
        System.out.print("Check realm - ")
        assertThat(Realm.getDefaultInstance(), `is`(mRealm))
        System.out.println("Success")
    }

    @Test
    fun checkCreateEntitiesMock() {
        System.out.print("Check character - ")
        val characterEntity = CHARACTER_ENTITY
        PowerMockito.`when`(mRealm.createObject(CharacterEntity::class.java)).thenReturn(characterEntity)
        val outputCharacter = mRealm.createObject(CharacterEntity::class.java)
        assertThat(outputCharacter, `is`(characterEntity))
        System.out.println("Success")

        System.out.print("Check mount - ")
        val mountEntity = MOUNT_ENTITY_1
        PowerMockito.`when`(mRealm.createObject(MountEntity::class.java)).thenReturn(mountEntity)
        val outputMount = mRealm.createObject(MountEntity::class.java)
        assertThat(outputMount, `is`(mountEntity))
        System.out.println("Success")
    }

    @Test
    fun get() {
        // Create a mock RealmQuery
        val characterQuery = PowerMockito.mock(RealmQuery::class.java) as RealmQuery<CharacterEntity>
        PowerMockito.`when`(characterQuery.findFirst()).thenReturn(CHARACTER_ENTITY)
        PowerMockito.`when`(mRealm.where(CharacterEntity::class.java)).thenReturn(characterQuery)

        val mountsResult = PowerMockito.mock(RealmResults::class.java) as RealmResults<MountEntity>
        PowerMockito.`when`(mountsResult.iterator()).thenReturn(MOUNT_ENTITY_LIST.toMutableList().iterator())
        PowerMockito.`when`(mountsResult.size).thenReturn(MOUNT_ENTITY_LIST.size)
        val array = Character("mock", "r")
        PowerMockito.whenNew(Character::class.java).withArguments(Mockito.anyString(), Mockito.anyString()).thenReturn(array)
        val array2 = Character("mock2", "r2")
//        PowerMockito.`when`(mountsResult.toList()).thenReturn(arrayListOf())
        val mountsQuery = PowerMockito.mock(RealmQuery::class.java) as RealmQuery<MountEntity>
        PowerMockito.`when`(mountsQuery.findAll()).thenReturn(mountsResult)
        PowerMockito.`when`(mRealm.where(MountEntity::class.java)).thenReturn(mountsQuery)

        val observer = mCharacterLocalStorage
                .get()
                .toObservable()
                .test()

        System.out.print("Check character is not null - ")
        observer.assertValue { it.first != null }
        System.out.println("Success")

        System.out.print("Check character - ")
        observer.assertValue {
            it.first!!.name == CHARACTER_ENTITY.name &&
                    it.first!!.realm == CHARACTER_ENTITY.realm &&
                    it.first!!.level == CHARACTER_ENTITY.level &&
                    it.first!!.thumbnail == CHARACTER_ENTITY.thumbnail &&
                    it.first!!.region == CHARACTER_ENTITY.region
        }
        System.out.println("Success")

        System.out.print("Check mount list size - ")
        observer.assertValue {
            it.second.size == MOUNT_ENTITY_LIST.size
        }
        System.out.println("Success")

//        System.out.print("Check mount 0 - ")
//        observer.assertValue {
//            it.second[0].run {
//                id == RealmMockRule.MOUNT_ENTITY_1.id &&
//                        name == RealmMockRule.MOUNT_ENTITY_1.name &&
//                        itemId == RealmMockRule.MOUNT_ENTITY_1.itemId &&
//                        qualityId == RealmMockRule.MOUNT_ENTITY_1.qualityId &&
//                        clientId == RealmMockRule.MOUNT_ENTITY_1.clientId &&
//                        icon == RealmMockRule.MOUNT_ENTITY_1.icon &&
//                        isGround == RealmMockRule.MOUNT_ENTITY_1.isGround &&
//                        isFlying == RealmMockRule.MOUNT_ENTITY_1.isFlying &&
//                        isAquatic == RealmMockRule.MOUNT_ENTITY_1.isAquatic &&
//                        isCollected == RealmMockRule.MOUNT_ENTITY_1.isCollected
//            }
//        }
//        System.out.println("Success")
//
//        System.out.print("Check mount 1 - ")
//        observer.assertValue {
//            it.second[1].run {
//                id == RealmMockRule.MOUNT_ENTITY_2.id &&
//                        name == RealmMockRule.MOUNT_ENTITY_2.name &&
//                        itemId == RealmMockRule.MOUNT_ENTITY_2.itemId &&
//                        qualityId == RealmMockRule.MOUNT_ENTITY_2.qualityId &&
//                        clientId == RealmMockRule.MOUNT_ENTITY_2.clientId &&
//                        icon == RealmMockRule.MOUNT_ENTITY_2.icon &&
//                        isGround == RealmMockRule.MOUNT_ENTITY_2.isGround &&
//                        isFlying == RealmMockRule.MOUNT_ENTITY_2.isFlying &&
//                        isAquatic == RealmMockRule.MOUNT_ENTITY_2.isAquatic &&
//                        isCollected == RealmMockRule.MOUNT_ENTITY_2.isCollected
//            }
//        }
//        System.out.println("Success")
//
//        System.out.print("Check mount 2 - ")
//        observer.assertValue {
//            it.second[2].run {
//                id == RealmMockRule.MOUNT_ENTITY_3.id &&
//                        name == RealmMockRule.MOUNT_ENTITY_3.name &&
//                        itemId == RealmMockRule.MOUNT_ENTITY_3.itemId &&
//                        qualityId == RealmMockRule.MOUNT_ENTITY_3.qualityId &&
//                        clientId == RealmMockRule.MOUNT_ENTITY_3.clientId &&
//                        icon == RealmMockRule.MOUNT_ENTITY_3.icon &&
//                        isGround == RealmMockRule.MOUNT_ENTITY_3.isGround &&
//                        isFlying == RealmMockRule.MOUNT_ENTITY_3.isFlying &&
//                        isAquatic == RealmMockRule.MOUNT_ENTITY_3.isAquatic &&
//                        isCollected == RealmMockRule.MOUNT_ENTITY_3.isCollected
//            }
//        }
//        System.out.println("Success")
//
//        System.out.print("Check mount 3 - ")
//        observer.assertValue {
//            it.second[3].run {
//                id == RealmMockRule.MOUNT_ENTITY_4.id &&
//                        name == RealmMockRule.MOUNT_ENTITY_4.name &&
//                        itemId == RealmMockRule.MOUNT_ENTITY_4.itemId &&
//                        qualityId == RealmMockRule.MOUNT_ENTITY_4.qualityId &&
//                        clientId == RealmMockRule.MOUNT_ENTITY_4.clientId &&
//                        icon == RealmMockRule.MOUNT_ENTITY_4.icon &&
//                        isGround == RealmMockRule.MOUNT_ENTITY_4.isGround &&
//                        isFlying == RealmMockRule.MOUNT_ENTITY_4.isFlying &&
//                        isAquatic == RealmMockRule.MOUNT_ENTITY_4.isAquatic &&
//                        isCollected == RealmMockRule.MOUNT_ENTITY_4.isCollected
//            }
//        }
//        System.out.println("Success")
    }

    @Test
    fun set() {
        // in create CharacterEntity return mock
        PowerMockito.`when`(mRealm.createObject(CharacterEntity::class.java)).thenReturn(PowerMockito.mock(CharacterEntity::class.java))
        // in create MountEntity return mock
        PowerMockito.`when`(mRealm.createObject(MountEntity::class.java)).thenReturn(PowerMockito.mock(MountEntity::class.java))
        // mock transaction execution
        PowerMockito.doNothing().`when`(mRealm).beginTransaction()
        PowerMockito.doNothing().`when`(mRealm).commitTransaction()
        // mock delete
        PowerMockito.doNothing().`when`(mRealm).delete(CharacterEntity::class.java)
        PowerMockito.doNothing().`when`(mRealm).delete(MountEntity::class.java)

        mCharacterLocalStorage
                .set(Pair(CHARACTER, MOUNT_LIST))
                .test()

        verify(mRealm, times(1)).beginTransaction()
        verify(mRealm, times(1)).delete(CharacterEntity::class.java)
        verify(mRealm, times(1)).createObject(CharacterEntity::class.java)
        verify(mRealm, times(1)).delete(MountEntity::class.java)
        verify(mRealm, times(4)).createObject(MountEntity::class.java)
        verify(mRealm, times(1)).commitTransaction()
        verify(mRealm, times(1)).close()
    }

    @Test
    fun clear() {
        mCharacterLocalStorage
                .clear()
                .test()
        verify(mRealm, times(1)).beginTransaction()
        verify(mRealm, times(1)).delete(CharacterEntity::class.java)
        verify(mRealm, times(1)).delete(MountEntity::class.java)
        verify(mRealm, times(1)).commitTransaction()
        verify(mRealm, times(1)).close()
    }
}