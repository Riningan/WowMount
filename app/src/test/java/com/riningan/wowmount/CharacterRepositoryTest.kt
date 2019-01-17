package com.riningan.wowmount

import com.riningan.wowmount.data.repository.CharacterRepository
import com.riningan.wowmount.data.repository.storage.local.CharacterLocalStorage
import com.riningan.wowmount.data.repository.storage.remote.CharacterRemoteStorage
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.superclasses
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

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
        resetCache()
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
        resetCache()
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


    private fun resetCache() {
        // reset time for reset cache life
        CharacterRepository::class.superclasses[0].declaredMembers.find {
            it.name == "mLastUpdateTime"
        }?.let {
            it.isAccessible = true
            (it as KMutableProperty1<CharacterRepository, Date>).set(mCharacterRepository, Date(1))
            it.isAccessible = false
        }
    }
}