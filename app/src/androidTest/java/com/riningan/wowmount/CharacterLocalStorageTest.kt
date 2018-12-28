package com.riningan.wowmount

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.riningan.wowmount.data.model.Character
import com.riningan.wowmount.data.model.Mount
import com.riningan.wowmount.data.storage.local.CharacterLocalStorage
import io.realm.Realm
import io.realm.RealmConfiguration
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Before


@RunWith(AndroidJUnit4::class)
class CharacterLocalStorageTest {
    private lateinit var mCharacterLocalStorage: CharacterLocalStorage
    private val mTestCharacter = Character().apply {
        name = "test"
    }
    private val mTestMounts = listOf(
            Mount().apply {
                name = "test mount 1"
            },
            Mount().apply {
                name = "test mount 2"
            })


    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getTargetContext()
        Realm.init(appContext)
        RealmConfiguration.Builder()
                .inMemory()
                .name("test-realm")
                .build()
        Realm.getDefaultInstance().apply {
            beginTransaction()
            copyToRealm(mTestCharacter)
            mTestMounts.forEach { copyToRealm(it) }
            commitTransaction()
            close()
        }
        mCharacterLocalStorage = CharacterLocalStorage()
    }

    @Test
    fun get() {
        val observer = mCharacterLocalStorage.get().toObservable()
                .test()
        observer.assertValue {
            it.first != null &&
                    it.first!!.hashCode() == mTestCharacter.hashCode() &&
                    it.second.size == mTestMounts.size &&
                    it.second.hashCode() == mTestMounts.hashCode()
        }
    }

    @Test
    fun clear() {
        mCharacterLocalStorage.get().test()
        val observer = mCharacterLocalStorage.get().toObservable()
                .test()
        observer.assertValue(Pair(null, listOf()))
    }
}
