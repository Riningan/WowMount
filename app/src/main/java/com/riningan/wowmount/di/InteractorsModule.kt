package com.riningan.wowmount.di

import com.riningan.wowmount.interactors.CharacterInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


val interactorsModule = Kodein.Module(name = "Interactors") {
    bind<CharacterInteractor>() with singleton { CharacterInteractor(Schedulers.io(), AndroidSchedulers.mainThread(), instance()) }
}