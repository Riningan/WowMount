package com.riningan.wowmount.app.di

import com.riningan.wowmount.data.network.BlizzardApiClient
import com.riningan.wowmount.data.network.BlizzardHttpClient
import com.riningan.wowmount.data.network.SpreadSheetApiClient
import com.riningan.wowmount.data.network.api.BlizzardApi
import com.riningan.wowmount.data.network.api.SpreadsheetApi
import okhttp3.OkHttpClient
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


fun getNetworkModule() = Kodein.Module(name = "Network") {
    bind<BlizzardHttpClient>() with singleton { BlizzardHttpClient(instance()) }
    bind<OkHttpClient>() with singleton { instance<BlizzardHttpClient>().getOkHttpClient() }
    bind<BlizzardApiClient>() with singleton { BlizzardApiClient(instance()) }
    bind<BlizzardApi>() with singleton { instance<BlizzardApiClient>().getClient() }
    bind<SpreadSheetApiClient>() with singleton { SpreadSheetApiClient() }
    bind<SpreadsheetApi>() with singleton { instance<SpreadSheetApiClient>().getClient() }
}