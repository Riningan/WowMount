package com.riningan.wowmount.app

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.riningan.util.Logger
import com.riningan.wowmount.BuildConfig
import com.riningan.wowmount.di.dataModule
import com.riningan.wowmount.di.dbModule
import com.riningan.wowmount.di.interactorsModule
import com.riningan.wowmount.di.networkModule
import com.riningan.wowmount.di.preferencesModule
import com.riningan.wowmount.di.presentersModule
import com.riningan.wowmount.di.routeModule
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
    }

    override val kodein = Kodein.lazy {
        bind<Context>() with singleton { this@WowMountApp }
        import(preferencesModule)
        import(networkModule)
        import(dbModule)
        import(dataModule)
        import(interactorsModule)
        import(routeModule)
        import(presentersModule)
    }


    companion object {
        private lateinit var mInstance: WowMountApp
        private lateinit var mRefWatcher: RefWatcher


        fun getContext() = mInstance

        fun getRefWatcher() = mRefWatcher
    }
}