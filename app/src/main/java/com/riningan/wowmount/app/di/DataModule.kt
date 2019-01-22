package com.riningan.wowmount.app.di

import com.riningan.wowmount.data.repository.CharacterRepository
import com.riningan.wowmount.data.repository.storage.local.CharacterLocalStorage
import com.riningan.wowmount.data.repository.storage.remote.CharacterRemoteStorage
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


fun getDataModule() = Kodein.Module(name = "Data") {
    bind<CharacterLocalStorage>() with singleton { CharacterLocalStorage(instance()) }
    bind<CharacterRemoteStorage>() with singleton { CharacterRemoteStorage(instance(), instance(), instance()) }
    bind<CharacterRepository>() with singleton { CharacterRepository(instance(), instance()) }
}