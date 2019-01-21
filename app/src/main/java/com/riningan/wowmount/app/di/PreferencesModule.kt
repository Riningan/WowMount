package com.riningan.wowmount.app.di

import com.riningan.wowmount.data.preferences.AppPreferences
import com.riningan.wowmount.data.preferences.LocalPreferences
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


var preferencesModule = Kodein.Module(name = "Preferences") {
    bind<LocalPreferences>() with singleton { AppPreferences(instance()) }
}