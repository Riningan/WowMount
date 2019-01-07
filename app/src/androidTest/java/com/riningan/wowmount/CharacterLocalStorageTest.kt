package com.riningan.wowmount

import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.riningan.wowmount.data.db.model.CharacterEntity
import com.riningan.wowmount.data.db.model.MountEntity
import com.riningan.wowmount.data.repository.storage.local.CharacterLocalStorage
import io.realm.Realm
import io.realm.RealmConfiguration
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class CharacterLocalStorageTest {
    private lateinit var mCharacterLocalStorage: CharacterLocalStorage
    private val mTestCharacter = CharacterEntity().apply {
        name = "test"
    }
    private val mTestMounts = listOf(
            MountEntity().apply {
                name = "test mount 1"
            },
            MountEntity().apply {
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