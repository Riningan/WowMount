package com.riningan.wowmount.presentation.ui

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.riningan.wowmount.app.WowMountApp
import com.riningan.wowmount.app.di.*
import com.riningan.wowmount.data.preferences.LocalPreferences
import com.riningan.wowmount.presentation.ui.splash.SplashFragment
import com.riningan.wowmount.test.TestActivity
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton


@RunWith(AndroidJUnit4::class)
class SplashFragmentTest {
    @get: Rule
    var mActivityTestRule = ActivityTestRule(TestActivity::class.java, true, false)


    @Before
    fun setup() {
        val mockedLocalPreferences = mockk<LocalPreferences>()
        every { mockedLocalPreferences.isActivated } returns false

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

        mActivityTestRule.launchActivity(Intent())
        mActivityTestRule.activity.setFragment(SplashFragment::class.java)
    }


    @Test
    fun mainActivityTest() {
//        await().atMost(5, TimeUnit.SECONDS).until(newUserWasAdded())
        Thread.sleep(10000)
//        onView(withId(R.id.container)).perform(click())
    }
}
