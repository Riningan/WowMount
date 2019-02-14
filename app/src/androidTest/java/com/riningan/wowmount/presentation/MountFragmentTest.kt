package com.riningan.wowmount.presentation

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withContentDescription
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.runner.AndroidJUnit4
import com.riningan.frarg.processor.MountFragmentArgs
import com.riningan.wowmount.R
import com.riningan.wowmount.dispatcher.Error401Dispatcher
import com.riningan.wowmount.dispatcher.ErrorDispatcher
import com.riningan.wowmount.dispatcher.RequestDispatcher
import com.riningan.wowmount.presentation.ui.mount.MountFragment
import okhttp3.mockwebserver.MockWebServer
import org.awaitility.Awaitility.await
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class MountFragmentTest : BaseTest() {
    private val mountFragmentArgs = MountFragmentArgs("only for animation", "Ковер-самолет/44554/3")
    private lateinit var mWebServer: MockWebServer


    @Before
    fun setup() {
        mWebServer = MockWebServer()
        mWebServer.start(8080)

        mAppRule.getMockedDI().setAuthorized()
    }

    @After
    fun teardown() {
        mWebServer.shutdown()
    }


    @Test
    fun checkLayout() {
        mWebServer.setDispatcher(RequestDispatcher())

        mAppRule.launch(MountFragment::class.java, mountFragmentArgs)

        waitCheckFragmentIsDisplayed(R.id.llMount)
    }

    @Test
    fun checkToolbar() {
        mWebServer.setDispatcher(RequestDispatcher())

        mAppRule.launch(MountFragment::class.java, mountFragmentArgs)

        waitCheckFragmentIsDisplayed(R.id.llMount)
        await().atMost(10, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(withId(R.id.tvMountName)).check(matches(withText("Ковер-самолет")))
                }
    }

    @Test
    fun error() {
        mWebServer.setDispatcher(ErrorDispatcher())

        mAppRule.launch(MountFragment::class.java, mountFragmentArgs)

        waitCheckFragmentIsDisplayed(R.id.llMount)
        waitCheckSnackbarIsDisplayed()
    }

    @Test
    fun error401() {
        mWebServer.setDispatcher(Error401Dispatcher())

        mAppRule.launch(MountFragment::class.java, mountFragmentArgs)

        waitCheckFragmentIsDisplayed(R.id.llMount)
        waitCheckSnackbarIsDisplayed()
        waitCheckFragmentIsDisplayed(R.id.cnslSplash)
    }

    @Test
    fun backClick() {
        mWebServer.setDispatcher(RequestDispatcher())

        mAppRule.launch(MountFragment::class.java, mountFragmentArgs)

        waitCheckFragmentIsDisplayed(R.id.llMount)
        onView(withContentDescription(R.string.mount_back)).perform(click())
        waitCheckExit()
    }
}