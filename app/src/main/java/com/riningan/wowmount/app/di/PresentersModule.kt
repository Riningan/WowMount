package com.riningan.wowmount.app.di

import com.riningan.wowmount.ui.mounts.MountsPresenter
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


fun getPresentersModule() = Kodein.Module(name = "Presenters") {
    bind<MountsPresenter>() with singleton { MountsPresenter(instance(), instance(), instance()) }
}