package com.riningan.wowmount.app.di

import android.content.Context
import com.riningan.wowmount.app.WowMountApp
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton


fun getContextModule() = Kodein.Module(name = "Context") {
    bind<Context>() with singleton { WowMountApp.getContext() }
}