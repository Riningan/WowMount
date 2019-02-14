package com.riningan.wowmount.presentation

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import com.riningan.frarg.processor.FilterFragmentArgs
import com.riningan.wowmount.R
import com.riningan.wowmount.dispatcher.RequestDispatcher
import com.riningan.wowmount.presentation.ui.filter.FilterFragment
import okhttp3.mockwebserver.MockWebServer
import org.awaitility.Awaitility.await
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit


class FilterFragmentTest : BaseTest() {
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
        mAppRule.launch(FilterFragment::class.java, FilterFragmentArgs(false))

        waitCheckFragmentIsDisplayed(R.id.cnslFilter)
    }

    @Test
    fun checkShow() {
        mWebServer.setDispatcher(RequestDispatcher())

        mAppRule.launch(FilterFragment::class.java, FilterFragmentArgs(false))

        waitCheckFragmentIsDisplayed(R.id.cnslFilter)

        onView(withId(R.id.pbFilter)).check(matches(isDisplayed()))
        onView(withId(R.id.btnFilterShow)).check(matches(not(isDisplayed())))
        onView(withId(R.id.cbFilterShowAll)).check(matches(not(isChecked())))

        await().atMost(10, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(withId(R.id.btnFilterShow)).check(matches(isDisplayed()))
                }

        onView(withId(R.id.pbFilter)).check(matches(not(isDisplayed())))
        onView(withId(R.id.btnFilterShow)).check(matches(withText("277")))
    }
}