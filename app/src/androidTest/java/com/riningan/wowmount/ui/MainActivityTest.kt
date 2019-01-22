package com.riningan.wowmount.ui

import android.content.Intent
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockkClass
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get: Rule
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java, true, false)


    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
//        val app = InstrumentationRegistry.getInstrumentation().targetContext as WowMountApp

//        mockkStatic("com.riningan.wowmount.app.di.PreferencesModuleKt")

//        preferencesModule = Kodein.Module(name = "Preferences") {
//            bind<LocalPreferences>() with singleton { object : LocalPreferences {
//                override var isActivated: Boolean
//                    get() = true
//                    set(value) {}
//                override var server: String
//                    get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
//                    set(value) {}
//                override var realmName: String
//                    get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
//                    set(value) {}
//                override var characterName: String
//                    get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
//                    set(value) {}
//                override var accessToken: String
//                    get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
//                    set(value) {}
//                override var showAll: Boolean
//                    get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
//                    set(value) {}
//
//                override fun clear() {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                }
//
//            } }
//        }

        val presenter = mockkClass(MainPresenter::class)
        every { presenter.startNavigation() } returns Unit

        mActivityTestRule.launchActivity(Intent())
    }


    @Test
    fun mainActivityTest() {
//        await().atMost(5, TimeUnit.SECONDS).until(newUserWasAdded())
        Thread.sleep(10000)
//        onView(withId(R.id.container)).perform(click())
    }
}
