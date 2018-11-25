package com.riningan.wowmount.di

import com.riningan.wowmount.data.CharacterRepository
import com.riningan.wowmount.data.LocalPreferences
import com.riningan.wowmount.data.MountsRepository
import io.realm.Realm
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val dataModule = Kodein.Module(name = "Data") {
    bind<LocalPreferences>() with singleton { LocalPreferences(instance()) }
    bind<Realm>() with singleton { Realm.getDefaultInstance() }
    bind<MountsRepository>() with singleton { MountsRepository(instance()) }
    bind<CharacterRepository>() with singleton { CharacterRepository(instance()) }
}