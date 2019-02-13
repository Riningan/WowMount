package com.riningan.wowmount.presentation

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.runner.AndroidJUnit4
import com.riningan.wowmount.R
import com.riningan.wowmount.presentation.ui.mounts.MountsFragment
import com.riningan.wowmount.rule.AppRule
import okhttp3.mockwebserver.MockWebServer
import org.awaitility.Awaitility.await
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class MountsFragmentTest {
    @get: Rule
    var mAppRule = AppRule()

    private lateinit var mWebServer: MockWebServer


    @Before
    fun setup() {
        mWebServer = MockWebServer()
        mWebServer.start(8080)
    }

    @After
    fun teardown() {
        mWebServer.shutdown()
    }


    @Test
    fun checkContainer() {
        onView(withId(android.R.id.content)).check(matches(isDisplayed()))
    }

    @Test
    fun checkLayout() {
        mAppRule.getMockedLocalPreferences().isActivated = false

        mAppRule.launch(MountsFragment::class.java)

        await().atMost(10, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(withId(R.id.crdlMounts)).check(matches(isDisplayed()))
                }
    }
}