package com.riningan.wowmount.app

import android.content.Context
import androidx.multidex.BuildConfig
import androidx.multidex.MultiDexApplication
import com.riningan.util.Logger
import com.riningan.wowmount.di.*
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import io.realm.Realm
import io.realm.RealmConfiguration
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton


class WowMountApp : MultiDexApplication(), KodeinAware {
    override fun onCreate() {
        super.onCreate()
        mInstance = this
        // Logger
        Logger.Config
                .removeApplicationId(BuildConfig.APPLICATION_ID)
                .addPreffix(BuildConfig.VERSION_NAME)
        // LeakCanary
        if (BuildConfig.DEBUG) {
            if (!LeakCanary.isInAnalyzerProcess(this)) {
                mRefWatcher = LeakCanary.install(this)
            }
        }
        // Realm
        Realm.init(this)
        val config = RealmConfiguration.Builder()
                .name("wowmount.realm")
                .schemaVersion(1)
                .build()
        Realm.setDefaultConfiguration(config)
    }

    override val kodein = Kodein.lazy {
        bind<Context>() with singleton { this@WowMountApp }
        import(networkModule)
        import(routeModule)
        import(dataModule)
        import(interactorsModule)
        import(presentersModule)
    }


    companion object {
        private lateinit var mInstance: WowMountApp
        private lateinit var mRefWatcher: RefWatcher


        fun getContext() = mInstance

        fun getRefWatcher() = mRefWatcher
    }
}