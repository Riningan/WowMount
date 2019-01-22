package com.riningan.wowmount.app.di

import com.riningan.wowmount.domain.interactor.CharacterInteractor
import com.riningan.wowmount.domain.SchedulersProvider
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton


fun getInteractorsModule() = Kodein.Module(name = "Interactors") {
    bind<SchedulersProvider>() with provider { SchedulersProvider() }
    bind<CharacterInteractor>() with singleton { CharacterInteractor(instance(), instance()) }
}