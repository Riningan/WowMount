package com.riningan.wowmount.storage

import com.riningan.wowmount.CHARACTER
import com.riningan.wowmount.CHARACTER_ENTITY
import com.riningan.wowmount.MOUNT_1
import com.riningan.wowmount.MOUNT_2
import com.riningan.wowmount.MOUNT_3
import com.riningan.wowmount.MOUNT_4
import com.riningan.wowmount.MOUNT_ENTITY_1
import com.riningan.wowmount.MOUNT_ENTITY_LIST
import com.riningan.wowmount.MOUNT_LIST
import com.riningan.wowmount.data.db.model.CharacterEntity
import com.riningan.wowmount.data.db.model.MountEntity
import com.riningan.wowmount.data.repository.storage.local.CharacterLocalStorage
import com.riningan.wowmount.rule.LogRule
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

/**
 * using PowerMock because Realm using static method
 */
@RunWith(PowerMockRunner::class)
@PowerMockRunnerDelegate(RobolectricTestRunner::class)
@Config(manifest = "AndroidManifest.xml", sdk = [21])
@PowerMockIgnore(value = ["org.mockito.*", "org.robolectric.*", "android.*"])
@SuppressStaticInitializationFor("io.realm.internal.Util")
@PrepareForTest(value = [Realm::class, RealmLog::class, RealmResults::class, RealmQuery::class])
class CharacterLocalStorageTest {
    @get: Rule
    var mPowerMockRule = PowerMockRule()
    @get: Rule
    val mLogRule = LogRule()

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
        assertThat(Realm.getDefaultInstance(), `is`(mRealm))
    }

    @Test
    fun checkCreateCharacterEntitiesMock() {
        val characterEntity = CHARACTER_ENTITY
        PowerMockito.`when`(mRealm.createObject(CharacterEntity::class.java)).thenReturn(characterEntity)
        val outputCharacter = mRealm.createObject(CharacterEntity::class.java)
        assertThat(outputCharacter, `is`(characterEntity))
    }

    @Test
    fun checkCreateMountEntitiesMock() {
        val mountEntity = MOUNT_ENTITY_1
        PowerMockito.`when`(mRealm.createObject(MountEntity::class.java)).thenReturn(mountEntity)
        val outputMount = mRealm.createObject(MountEntity::class.java)
        assertThat(outputMount, `is`(mountEntity))
    }

    /**
     * @see CharacterLocalStorage.get()
     */
    @Test
    fun get() {
        // Create a mock RealmQuery
        val characterQuery = PowerMockito.mock(RealmQuery::class.java) as RealmQuery<CharacterEntity>
        PowerMockito.`when`(characterQuery.findFirst()).thenReturn(CHARACTER_ENTITY)
        PowerMockito.`when`(mRealm.where(CharacterEntity::class.java)).thenReturn(characterQuery)
        val mountsResult = PowerMockito.mock(RealmResults::class.java) as RealmResults<MountEntity>
        PowerMockito.`when`(mountsResult.iterator()).thenReturn(MOUNT_ENTITY_LIST.toMutableList().iterator())
        PowerMockito.`when`(mountsResult.size).thenReturn(MOUNT_ENTITY_LIST.size)
        // mock toList()
        PowerMockito.`when`(mountsResult.toArray()).thenReturn(MOUNT_ENTITY_LIST.toTypedArray())
        val mountsQuery = PowerMockito.mock(RealmQuery::class.java) as RealmQuery<MountEntity>
        PowerMockito.`when`(mountsQuery.findAll()).thenReturn(mountsResult)
        PowerMockito.`when`(mRealm.where(MountEntity::class.java)).thenReturn(mountsQuery)

        mCharacterLocalStorage
                .get()
                .toObservable()
                .test()
                .assertValue { it.first != null }
                .assertValue { it.first!!.toString() == CHARACTER.toString() }
                .assertValue { it.second.size == MOUNT_ENTITY_LIST.size }
                .assertValue { it.second[0].toString() == MOUNT_1.toString() }
                .assertValue { it.second[1].toString() == MOUNT_2.toString() }
                .assertValue { it.second[2].toString() == MOUNT_3.toString() }
                .assertValue { it.second[3].toString() == MOUNT_4.toString() }
        verify(mRealm, times(1)).where(CharacterEntity::class.java)
        verify(mRealm, times(1)).where(MountEntity::class.java)
        verify(mRealm, times(1)).close()
    }

    /**
     * @see CharacterLocalStorage.set()
     */
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

    /**
     * @see CharacterLocalStorage.clear()
     */
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