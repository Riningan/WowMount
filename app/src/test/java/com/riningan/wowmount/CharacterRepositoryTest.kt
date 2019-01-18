package com.riningan.wowmount

import com.riningan.wowmount.data.repository.CharacterRepository
import com.riningan.wowmount.data.repository.model.Mount
import com.riningan.wowmount.data.repository.storage.local.CharacterLocalStorage
import com.riningan.wowmount.data.repository.storage.remote.CharacterRemoteStorage
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.superclasses
import kotlin.reflect.jvm.isAccessible

/**
 * using MockK because
 * @see CharacterLocalStorage.set
 * using generic parameter
 */
class CharacterRepositoryTest {
    @get: Rule
    val logRule = LogRule()

    private lateinit var mCharacterLocalStorage: CharacterLocalStorage
    private lateinit var mCharacterRemoteStorage: CharacterRemoteStorage
    private lateinit var mCharacterRepository: CharacterRepository


    @Before
    fun setup() {
        mCharacterLocalStorage = mockk()
        every { mCharacterLocalStorage.clear() } returns Completable.complete()
        every { mCharacterLocalStorage.set(any()) } returns Completable.complete()
        every { mCharacterLocalStorage.get() } returns Single.fromCallable { Pair(CHARACTER, listOf(MOUNT_1, MOUNT_2)) }

        mCharacterRemoteStorage = mockk()
        every { mCharacterRemoteStorage.get() } returns Single.fromCallable { Pair(CHARACTER, listOf(MOUNT_1, MOUNT_2, MOUNT_3)) }

        mCharacterRepository = CharacterRepository(mCharacterLocalStorage, mCharacterRemoteStorage)
    }


    @Test
    fun update() {
        mCharacterRepository
                .update()
                .test()
                .assertValue { it.first != null }
                .assertValue { it.first!!.toString() == CHARACTER.toString() }
                .assertValue { it.second.size == MOUNT_ENTITY_LIST.size }
                .assertValue { it.second[0].toString() == MOUNT_1.toString() }
                .assertValue { it.second[1].toString() == MOUNT_2.toString() }
                .assertValue { it.second[2].toString() == MOUNT_3.toString() }
                .assertValue { it.second[3].toString() == MOUNT_4.toString() }
        verify(exactly = 1) { mCharacterRemoteStorage.get() }
        verify(exactly = 1) { mCharacterLocalStorage.set(any()) }
    }

    @Test
    fun get() {
        // first call
        // first read from local storage
        // second read from remote storage
        mCharacterRepository
                .get()
                .test()
                .assertValueCount(2)
                .assertValueAt(0) { it.first != null }
                .assertValueAt(0) { it.first!!.toString() == CHARACTER.toString() }
                .assertValueAt(0) { it.second.size == 2 }
                .assertValueAt(0) { it.second[0].toString() == MOUNT_1.toString() }
                .assertValueAt(0) { it.second[1].toString() == MOUNT_2.toString() }
                .assertValueAt(1) { it.first != null }
                .assertValueAt(1) { it.first!!.toString() == CHARACTER.toString() }
                .assertValueAt(1) { it.second.size == 3 }
                .assertValueAt(1) { it.second[0].toString() == MOUNT_1.toString() }
                .assertValueAt(1) { it.second[1].toString() == MOUNT_2.toString() }
                .assertValueAt(1) { it.second[2].toString() == MOUNT_3.toString() }
        verifySequence {
            mCharacterLocalStorage.get()
            mCharacterRemoteStorage.get()
            mCharacterLocalStorage.set(any())
        }
        // second call
        // read only memory cache
        mCharacterRepository
                .get()
                .test()
                .assertValueCount(1)
                .assertValue { it.first != null }
                .assertValue { it.first!!.toString() == CHARACTER.toString() }
                .assertValue { it.second.size == 3 }
                .assertValue { it.second[0].toString() == MOUNT_1.toString() }
                .assertValue { it.second[1].toString() == MOUNT_2.toString() }
                .assertValue { it.second[2].toString() == MOUNT_3.toString() }
        // methods not called
        verify(exactly = 1) { mCharacterLocalStorage.get() }
        verify(exactly = 1) { mCharacterRemoteStorage.get() }
        verify(exactly = 1) { mCharacterLocalStorage.set(any()) }
        // reset time for reset cache life
        setCacheTrusted(false)
        // third call
        // read from memory cache
        // read from remote storage (NOT changed)
        // they are equals
        // onNext calls one time
        mCharacterRepository
                .get()
                .test()
                .assertValueCount(1)
                .assertValue { it.first != null }
                .assertValue { it.first!!.toString() == CHARACTER.toString() }
                .assertValue { it.second.size == 3 }
                .assertValue { it.second[0].toString() == MOUNT_1.toString() }
                .assertValue { it.second[1].toString() == MOUNT_2.toString() }
                .assertValue { it.second[2].toString() == MOUNT_3.toString() }
        verify(exactly = 1) { mCharacterLocalStorage.get() }
        verify(exactly = 2) { mCharacterRemoteStorage.get() }
        verify(exactly = 2) { mCharacterLocalStorage.set(any()) }
        // change remote storage
        every { mCharacterRemoteStorage.get() } returns Single.fromCallable { Pair(CHARACTER, MOUNT_LIST) }
        // reset time for reset cache life
        setCacheTrusted(false)
        // forth call
        // read from memory cache
        // read from remote storage
        // they are not equals
        // onNext calls twice
        mCharacterRepository
                .get()
                .test()
                .assertValueCount(2)
                .assertValueAt(0) { it.first != null }
                .assertValueAt(0) { it.first!!.toString() == CHARACTER.toString() }
                .assertValueAt(0) { it.second.size == 3 }
                .assertValueAt(0) { it.second[0].toString() == MOUNT_1.toString() }
                .assertValueAt(0) { it.second[1].toString() == MOUNT_2.toString() }
                .assertValueAt(0) { it.second[2].toString() == MOUNT_3.toString() }
                .assertValueAt(1) { it.first != null }
                .assertValueAt(1) { it.first!!.toString() == CHARACTER.toString() }
                .assertValueAt(1) { it.second.size == MOUNT_LIST.size }
                .assertValueAt(1) { it.second[0].toString() == MOUNT_1.toString() }
                .assertValueAt(1) { it.second[1].toString() == MOUNT_2.toString() }
                .assertValueAt(1) { it.second[2].toString() == MOUNT_3.toString() }
                .assertValueAt(1) { it.second[3].toString() == MOUNT_4.toString() }
        verify(exactly = 1) { mCharacterLocalStorage.get() }
        verify(exactly = 3) { mCharacterRemoteStorage.get() }
        verify(exactly = 3) { mCharacterLocalStorage.set(any()) }
    }

    @Test
    fun clear() {
        mCharacterRepository
                .clear()
                .test()

        verify(exactly = 1) { mCharacterLocalStorage.clear() }
    }

    /**
     * first call
     * no cache
     * get existing mount from local
     * get existing mount from remote
     */
    @Test
    fun getMountById1() {
        mCharacterRepository
                .getMountById(MOUNT_2.id)
                .test()
                .assertValueCount(2)
                .assertValueAt(0) { it.toString() == MOUNT_2.toString() }
                .assertValueAt(1) { it.toString() == MOUNT_2.toString() }
        verifySequence {
            mCharacterLocalStorage.get()
            mCharacterRemoteStorage.get()
            mCharacterLocalStorage.set(any())
        }
    }

    /**
     * second call
     * cache is trusted
     * get existing mount from cache
     */
    @Test
    fun getMountById2() {
        setCache(listOf(MOUNT_1, MOUNT_2))
        setCacheTrusted(true)
        mCharacterRepository
                .getMountById(MOUNT_2.id)
                .test()
                .assertValueCount(1)
                .assertValueAt(0) { it.toString() == MOUNT_2.toString() }
    }

    /**
     * third call
     * cache is trusted
     * get NOT existing mount from cache
     */
    @Test
    fun getMountById3() {
        setCache(listOf(MOUNT_1, MOUNT_2))
        setCacheTrusted(true)
        mCharacterRepository
                .getMountById(MOUNT_4.id)
                .test()
                .assertFailure(NullPointerException::class.java)
    }

    /**
     * forth call
     * cache is NOT trusted
     * get NOT existing mount from cache and remote
     */
    @Test
    fun getMountById4() {
        setCache(listOf(MOUNT_1, MOUNT_2))
        setCacheTrusted(false)
        mCharacterRepository
                .getMountById(MOUNT_4.id)
                .test()
                .assertFailure(NullPointerException::class.java)
        verifySequence {
            mCharacterRemoteStorage.get()
        }
    }

    /**
     * fifth call
     * cache is NOT trusted
     * get NOT existing mount from cache
     * but existing in remote
     */
    @Test
    fun getMountById5() {
        setCache(listOf(MOUNT_1, MOUNT_2))
        setCacheTrusted(false)
        every { mCharacterRemoteStorage.get() } returns Single.fromCallable { Pair(CHARACTER, MOUNT_LIST) }
        mCharacterRepository
                .getMountById(MOUNT_4.id)
                .test()
                .assertValueCount(1)
                .assertValue { it.toString() == MOUNT_4.toString() }
        verifySequence {
            mCharacterRemoteStorage.get()
            mCharacterLocalStorage.set(any())
        }
    }

    /**
     * sixth call
     * cache is trusted
     * get existing mount from cache
     */
    @Test
    fun getMountById6() {
        setCache(MOUNT_LIST)
        setCacheTrusted(true)
        mCharacterRepository
                .getMountById(MOUNT_4.id)
                .test()
                .assertValueCount(1)
                .assertValue { it.toString() == MOUNT_4.toString() }
    }


    /**
     * reset time for reset cache trust
     */
    private fun setCacheTrusted(trust: Boolean) {
        setPrivateField("mLastUpdateTime", Date(if (trust) Long.MAX_VALUE else 1))
    }

    private fun setCache(mounts: List<Mount>) {
        setPrivateField("mCache", Pair(CHARACTER, mounts))
    }

    private fun <T> setPrivateField(fieldName: String, value: T) {
        CharacterRepository::class.superclasses[0].declaredMembers.find {
            it.name == fieldName
        }?.let {
            it.isAccessible = true
            (it as KMutableProperty1<CharacterRepository, T>).set(mCharacterRepository, value)
            it.isAccessible = false
        }
    }
}