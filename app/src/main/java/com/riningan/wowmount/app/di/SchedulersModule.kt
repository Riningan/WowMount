package com.riningan.wowmount.app.di

import com.riningan.wowmount.utils.SchedulersProvider
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider


val schedulersModule = Kodein.Module(name = "Schedulers") {
    bind<SchedulersProvider>() with provider { SchedulersProvider() }
}