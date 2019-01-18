package com.riningan.wowmount

import com.riningan.util.Log
import com.riningan.util.Logger
import com.riningan.wowmount.data.repository.CharacterRepository
import com.riningan.wowmount.interactors.CharacterInteractor
import com.riningan.wowmount.interactors.WowMountExceptions
import com.riningan.wowmount.utils.DeviceUtil
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import kotlin.reflect.full.functions
import kotlin.reflect.jvm.isAccessible

/**
 * using MockK because WowMountExceptions has Companion that need mock
 */
class CharacterInteractorTest {
    @get: Rule
    val mLogRule = LogRule()
    private lateinit var mCharacterRepository: CharacterRepository
    private lateinit var mCharacterInteractor: CharacterInteractor


    @Before
    fun setup() {
        // Mock Logger.class
        val log: Log = mockk(relaxed = true)
        mockkStatic(Logger::class)
        every { Logger.forThis(any()) } returns log
        // Mock WowMountExceptions.class
        mockkObject(WowMountExceptions)
        every {
            WowMountExceptions.Companion::class.functions.find {
                it.name == "getApplicationMessage"
            }!!.apply {
                isAccessible = true
            }.call(WowMountExceptions, any<WowMountExceptions>())
        } returns "mocked"

        mCharacterRepository = mockk()
        val testScheduler = Schedulers.trampoline()
        mCharacterInteractor = CharacterInteractor(testScheduler, testScheduler, mCharacterRepository)
    }


    /**
     * normal values
     */
    @Test
    fun update_1() {
        every { mCharacterRepository.update() } returns Single.just(Pair(CHARACTER, MOUNT_LIST))
        mCharacterInteractor
                .update()
                .test()
                .assertValueCount(1)
                .assertValue { Pair(CHARACTER, MOUNT_LIST).toString() == it.toString() }
    }

    /**
     * null character
     */
    @Test
    fun update_2() {
        every { mCharacterRepository.update() } returns Single.just(Pair(null, MOUNT_LIST))
        mCharacterInteractor
                .update()
                .test()
                .assertFailure(WowMountExceptions.ApplicationException::class.java)
    }

    /**
     * One time emit onNext
     */
    @Test
    fun get_1() {
        every { mCharacterRepository.get() } returns Flowable.just(Pair(CHARACTER, MOUNT_LIST))
        mCharacterInteractor
                .get()
                .test()
                .assertValueCount(1)
                .assertValue { Pair(CHARACTER, MOUNT_LIST).toString() == it.toString() }
    }

    /**
     * Twice emit onNext
     */
    @Test
    fun get_2() {
        every { mCharacterRepository.get() } returns Flowable.just(Pair(CHARACTER, listOf(MOUNT_1, MOUNT_2)), Pair(CHARACTER, MOUNT_LIST))
        mCharacterInteractor
                .get()
                .test()
                .assertValueCount(2)
                .assertValueAt(0) { Pair(CHARACTER, listOf(MOUNT_1, MOUNT_2)).toString() == it.toString() }
                .assertValueAt(1) { Pair(CHARACTER, MOUNT_LIST).toString() == it.toString() }
    }

    /**
     * Twice emit onNext, but on the first time it contain null character
     */
    @Test
    fun get_3() {
        every { mCharacterRepository.get() } returns Flowable.just(Pair(null, listOf()), Pair(CHARACTER, MOUNT_LIST))
        mCharacterInteractor
                .get()
                .test()
                .assertFailure(WowMountExceptions.ApplicationException::class.java)
    }

    /**
     * Twice emit onNext, but on the second time it throw exception
     */
    @Test
    fun get_4() {
        mockkObject(DeviceUtil)
        every { DeviceUtil.isOnline() } returns false
        every { mCharacterRepository.get() } returns Flowable.merge(Flowable.just(Pair(CHARACTER, MOUNT_LIST)), Flowable.error(IOException()))
        mCharacterInteractor
                .get()
                .test()
                .assertFailure(WowMountExceptions.NoInternetException::class.java, Pair(CHARACTER, MOUNT_LIST))
                .assertNotComplete()
    }

    @Test
    fun clear() {
        every { mCharacterRepository.clear() } returns Completable.complete()
        mCharacterInteractor
                .clear()
                .test()
                .assertNoErrors()
    }

    @Test
    fun getMountById() {
        every { mCharacterRepository.getMountById(MOUNT_1.id) } returns Flowable.just(MOUNT_1)
        mCharacterInteractor
                .getMountById(MOUNT_1.id)
                .test()
                .assertValue(MOUNT_1)
    }
}