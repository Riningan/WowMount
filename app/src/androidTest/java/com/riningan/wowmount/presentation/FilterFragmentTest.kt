package com.riningan.wowmount.presentation

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isChecked
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withContentDescription
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.runner.AndroidJUnit4
import com.riningan.frarg.processor.FilterFragmentArgs
import com.riningan.wowmount.R
import com.riningan.wowmount.dispatcher.Error401Dispatcher
import com.riningan.wowmount.dispatcher.ErrorDispatcher
import com.riningan.wowmount.dispatcher.RequestDispatcher
import com.riningan.wowmount.presentation.ui.filter.FilterFragment
import okhttp3.mockwebserver.MockWebServer
import org.awaitility.Awaitility.await
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
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
    fun checkedShowAll() {
        mWebServer.setDispatcher(RequestDispatcher())

        mAppRule.launch(FilterFragment::class.java, FilterFragmentArgs(true))

        waitCheckFragmentIsDisplayed(R.id.cnslFilter)
        onView(withId(R.id.pbFilter)).check(matches(isDisplayed()))
        onView(withId(R.id.btnFilterShow)).check(matches(not(isDisplayed())))
        onView(withId(R.id.cbFilterShowAll)).check(matches(isChecked()))
        await().atMost(10, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(withId(R.id.btnFilterShow)).check(matches(isDisplayed()))
                }
        onView(withId(R.id.pbFilter)).check(matches(not(isDisplayed())))
        onView(withId(R.id.btnFilterShow))
                .check(matches(withText(mAppRule.getActivity().getString(R.string.filter_show_with_count, 711))))
                .perform(click())
        waitCheckFragmentIsDisplayed(R.id.crdlMounts)
    }

    @Test
    fun checkedShowCollected() {
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
        onView(withId(R.id.btnFilterShow))
                .check(matches(withText(mAppRule.getActivity().getString(R.string.filter_show_with_count, 277))))
                .perform(click())
        waitCheckFragmentIsDisplayed(R.id.crdlMounts)
    }

    @Test
    fun checkedChangeWhenButtonVisible() {
        mWebServer.setDispatcher(RequestDispatcher())

        mAppRule.launch(FilterFragment::class.java, FilterFragmentArgs(false))

        waitCheckFragmentIsDisplayed(R.id.cnslFilter)
        onView(withId(R.id.pbFilter)).check(matches(isDisplayed()))
        onView(withId(R.id.btnFilterShow)).check(matches(not(isDisplayed())))
        await().atMost(10, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(withId(R.id.btnFilterShow)).check(matches(isDisplayed()))
                }
        onView(withId(R.id.pbFilter)).check(matches(not(isDisplayed())))
        for (i in 1..5) {
            onView(withId(R.id.cbFilterShowAll)).perform(click())
            onView(withId(R.id.pbFilter)).check(matches(isDisplayed()))
            onView(withId(R.id.btnFilterShow)).check(matches(not(isDisplayed())))
            await().atMost(10, TimeUnit.SECONDS)
                    .ignoreExceptions()
                    .untilAsserted {
                        onView(withId(R.id.btnFilterShow)).check(matches(isDisplayed()))
                    }
            onView(withId(R.id.pbFilter)).check(matches(not(isDisplayed())))
        }
    }

    @Test
    fun checkedChangeQuickly() {
        mWebServer.setDispatcher(RequestDispatcher())

        mAppRule.launch(FilterFragment::class.java, FilterFragmentArgs(false))

        waitCheckFragmentIsDisplayed(R.id.cnslFilter)
        for (i in 1..5) {
            onView(withId(R.id.cbFilterShowAll)).perform(click())
            onView(withId(R.id.pbFilter)).check(matches(isDisplayed()))
            onView(withId(R.id.btnFilterShow)).check(matches(not(isDisplayed())))
        }
    }

    @Test
    fun updateMountsError() {
        mWebServer.setDispatcher(ErrorDispatcher())

        mAppRule.launch(FilterFragment::class.java, FilterFragmentArgs(false))

        waitCheckFragmentIsDisplayed(R.id.cnslFilter)
        onView(withId(R.id.pbFilter)).check(matches(isDisplayed()))
        onView(withId(R.id.btnFilterShow)).check(matches(not(isDisplayed())))
        waitCheckSnackbarIsDisplayed()
        onView(withId(R.id.pbFilter)).check(matches(not(isDisplayed())))
        onView(withId(R.id.btnFilterShow)).check(matches(withText(mAppRule.getActivity().getString(R.string.filter_show_without_count))))
    }

    @Test
    fun updateMountsError401() {
        mWebServer.setDispatcher(Error401Dispatcher())

        mAppRule.launch(FilterFragment::class.java, FilterFragmentArgs(false))

        waitCheckFragmentIsDisplayed(R.id.cnslFilter)
        onView(withId(R.id.pbFilter)).check(matches(isDisplayed()))
        onView(withId(R.id.btnFilterShow)).check(matches(not(isDisplayed())))
        waitCheckSnackbarIsDisplayed()
        waitCheckFragmentIsDisplayed(R.id.cnslSplash)
    }

    @Test
    fun backClick() {
        mAppRule.getMockedDI().setAuthorized()

        mAppRule.launch(FilterFragment::class.java, FilterFragmentArgs(false))

        waitCheckFragmentIsDisplayed(R.id.cnslFilter)
        onView(withContentDescription(R.string.filter_back)).perform(click())
        waitCheckExit()
    }
}