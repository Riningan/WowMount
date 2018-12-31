package com.riningan.wowmount.di

import com.riningan.wowmount.data.LocalPreferences
import com.riningan.wowmount.data.repository.CharacterRepository
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


val dataModule = Kodein.Module(name = "Data") {
    bind<LocalPreferences>() with singleton { LocalPreferences(instance()) }
    bind<CharacterRepository>() with singleton { CharacterRepository(instance(), instance()) }
}