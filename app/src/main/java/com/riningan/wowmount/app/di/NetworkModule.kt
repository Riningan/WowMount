package com.riningan.wowmount.app.di

import com.riningan.wowmount.data.network.ApiClient
import com.riningan.wowmount.data.network.api.BlizzardApi
import com.riningan.wowmount.data.network.api.SpreadsheetApi
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


fun getNetworkModule() = Kodein.Module(name = "Network") {
    bind<ApiClient>() with singleton { ApiClient(instance()) }
    bind<BlizzardApi>() with singleton { instance<ApiClient>().getBlizzardApi() }
    bind<SpreadsheetApi>() with singleton { instance<ApiClient>().getSpreadsheetApi() }
}