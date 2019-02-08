package com.riningan.wowmount.app

import android.os.StrictMode
import androidx.multidex.MultiDexApplication
import com.riningan.util.Logger
import com.riningan.wowmount.BuildConfig
import com.riningan.wowmount.app.di.*
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import org.kodein.di.KodeinAware
import org.kodein.di.conf.ConfigurableKodein


class WowMountApp : MultiDexApplication(), KodeinAware {
    override val kodein = ConfigurableKodein(mutable = true)


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
        // kodein
        kodein.apply {
            clear()
            addImport(getContextModule())
            addImport(getPreferencesModule())
            addImport(getNetworkModule())
            addImport(getDBModule())
            addImport(getDataModule())
            addImport(getInteractorsModule())
            addImport(getRouteModule())
            addImport(getPresentersModule())
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