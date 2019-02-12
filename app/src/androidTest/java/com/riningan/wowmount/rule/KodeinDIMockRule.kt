package com.riningan.wowmount.rule

import android.support.test.InstrumentationRegistry
import com.riningan.retrofit2.converter.csv.CsvConverterFactory
import com.riningan.wowmount.app.WowMountApp
import com.riningan.wowmount.app.di.*
import com.riningan.wowmount.data.db.DBHelper
import com.riningan.wowmount.data.network.BlizzardApiClient
import com.riningan.wowmount.data.network.TIMEOUT
import com.riningan.wowmount.data.network.api.BlizzardApi
import com.riningan.wowmount.data.network.api.SpreadsheetApi
import com.riningan.wowmount.data.preferences.LocalPreferences
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import okhttp3.OkHttpClient
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit


class KodeinDIMockRule : TestRule {
    val localPreferences = object : LocalPreferences {
        override var isActivated: Boolean = false
        override var server: String = ""
        override var realmName: String = ""
        override var characterName: String = ""
        override var accessToken: String = ""
        override var showAll: Boolean = true


        override fun clear() {
            isActivated = false
            server = ""
            realmName = ""
            characterName = ""
        }
    }

    private val mOkHttpClient = OkHttpClient().newBuilder()
            .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
            .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                        .header("Content-Type", "application/json")
                        .url(original.url().url().toString().replace("https://eu.api.blizzard.com", LOCALHOST))
                        .method(original.method(), original.body())
                chain.proceed(requestBuilder.build())
            }
            .build()

    private val mSpreadsheetApi = Retrofit.Builder()
            .baseUrl(LOCALHOST)
            .client(OkHttpClient().newBuilder()
                    .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                    .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                    .build())
            .addConverterFactory(CsvConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
            .create(SpreadsheetApi::class.java)

    private val mDBHelper = object : DBHelper {
        init {
//            Realm.init(InstrumentationRegistry.getInstrumentation().targetContext)
//            RealmConfiguration.Builder()
//                    .inMemory()
//                    .name("test-realm")
//                    .build()
        }

        override fun getDBInstance(): Realm = Realm.getDefaultInstance()
    }


    override fun apply(base: Statement, description: Description?) = object : Statement() {
        override fun evaluate() {
            val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as WowMountApp
            app.kodein.apply {
                clear()
                addImport(getContextModule())
                addImport(Kodein.Module("Preferences") {
                    bind<LocalPreferences>() with singleton { localPreferences }
                })
                addImport(Kodein.Module(name = "Network") {
                    bind<BlizzardApiClient>() with singleton { BlizzardApiClient(mOkHttpClient) }
                    bind<BlizzardApi>() with singleton { instance<BlizzardApiClient>().getClient() }
                    bind<SpreadsheetApi>() with singleton { mSpreadsheetApi }
                })
                addImport(Kodein.Module(name = "DB") {
                    bind<DBHelper>() with singleton { mDBHelper }
                })
                addImport(getDataModule())
                addImport(getInteractorsModule())
                addImport(getRouteModule())
                addImport(getPresentersModule())
            }
            base.evaluate()
        }
    }


    companion object {
        private const val LOCALHOST = "http://localhost:8080"
    }
}