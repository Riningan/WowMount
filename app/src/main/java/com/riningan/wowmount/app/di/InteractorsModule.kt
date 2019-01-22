package com.riningan.wowmount.app.di

import com.riningan.wowmount.interactor.CharacterInteractor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


fun getInteractorsModule() = Kodein.Module(name = "Interactors") {
    bind<CharacterInteractor>() with singleton { CharacterInteractor(instance(), instance()) }
}