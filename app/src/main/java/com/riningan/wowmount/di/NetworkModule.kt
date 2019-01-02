package com.riningan.wowmount.di

import com.riningan.wowmount.data.network.ApiClient
import com.riningan.wowmount.data.network.BlizzardApi
import com.riningan.wowmount.data.network.SpreadsheetApi
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


val networkModule = Kodein.Module(name = "Network") {
    bind<ApiClient>() with singleton { ApiClient(instance()) }
    bind<BlizzardApi>() with singleton { instance<ApiClient>().getBlizzardApi() }
    bind<SpreadsheetApi>() with singleton { instance<ApiClient>().getSpreadsheetApi() }
}