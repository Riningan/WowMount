package com.riningan.wowmount.presentation.ui

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.riningan.wowmount.app.WowMountApp
import com.riningan.wowmount.presentation.ui.authorization.AuthorizationFragment
import com.riningan.wowmount.test.TestActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.generic.instance
import ru.terrakok.cicerone.Router


@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get: Rule
    var mActivityTestRule = ActivityTestRule(TestActivity::class.java, true, false)

    @Before
    fun setup() {
//        MockKAnnotations.init(this, relaxUnitFun = true)
        val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as WowMountApp

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

        mActivityTestRule.launchActivity(Intent())
        mActivityTestRule.activity.setFragment(AuthorizationFragment::class.java)
    }


    @Test
    fun mainActivityTest() {
//        await().atMost(5, TimeUnit.SECONDS).until(newUserWasAdded())
        Thread.sleep(10000)
//        onView(withId(R.id.container)).perform(click())
    }
}
