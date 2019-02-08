package com.riningan.wowmount.rule

import android.support.test.InstrumentationRegistry
import com.riningan.wowmount.app.WowMountApp
import com.riningan.wowmount.app.di.getContextModule
import com.riningan.wowmount.app.di.getDBModule
import com.riningan.wowmount.app.di.getDataModule
import com.riningan.wowmount.app.di.getInteractorsModule
import com.riningan.wowmount.app.di.getNetworkModule
import com.riningan.wowmount.app.di.getPresentersModule
import com.riningan.wowmount.app.di.getRouteModule
import com.riningan.wowmount.data.preferences.LocalPreferences
import io.mockk.mockk
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton


class KodeinDIMockRule : TestRule {
    val mockedLocalPreferences = mockk<LocalPreferences>()


    override fun apply(base: Statement, description: Description?) = object : Statement() {
        override fun evaluate() {
            val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as WowMountApp
            app.kodein.apply {
                clear()
                addImport(getContextModule())
                addImport(Kodein.Module("Preferences") {
                    bind<LocalPreferences>() with singleton { mockedLocalPreferences }
                })
                addImport(getNetworkModule())
                addImport(getDBModule())
                addImport(getDataModule())
                addImport(getInteractorsModule())
                addImport(getRouteModule())
                addImport(getPresentersModule())
            }
            base.evaluate()
        }
    }
}