package com.riningan.wowmount.app

import android.content.Context
import android.os.StrictMode
import androidx.multidex.MultiDexApplication
import com.riningan.util.Logger
import com.riningan.wowmount.BuildConfig
import com.riningan.wowmount.di.*
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton


class WowMountApp : MultiDexApplication(), KodeinAware {
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


    override fun onCreate() {
        super.onCreate()
        mInstance = this
        // Logger
        Logger.Config
                .removeApplicationId(BuildConfig.APPLICATION_ID)
                .addPreffix(BuildConfig.VERSION_NAME)
        // LeakCanary
        if (BuildConfig.DEBUG) {
            enabledStrictMode()
            if (!LeakCanary.isInAnalyzerProcess(this)) {
                mRefWatcher = LeakCanary.install(this)
            }
        }
    }


    companion object {
        private lateinit var mInstance: WowMountApp
        private lateinit var mRefWatcher: RefWatcher


        fun getContext() = mInstance

        fun getRefWatcher() = mRefWatcher


        private fun enabledStrictMode() {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath()
                    .build())
        }
    }
}