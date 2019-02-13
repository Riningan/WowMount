package com.riningan.wowmount.presentation

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewAssertion
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.contrib.ViewPagerActions.scrollLeft
import android.support.test.espresso.contrib.ViewPagerActions.scrollRight
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.riningan.frarg.processor.MountsFragmentArgs
import com.riningan.wowmount.R
import com.riningan.wowmount.dispatcher.Error401Dispatcher
import com.riningan.wowmount.dispatcher.ErrorDispatcher
import com.riningan.wowmount.dispatcher.RequestDispatcher
import com.riningan.wowmount.presentation.ui.mounts.ItemsAdapter
import com.riningan.wowmount.presentation.ui.mounts.MountsFragment
import com.riningan.wowmount.rule.AppRule
import okhttp3.mockwebserver.MockWebServer
import org.awaitility.Awaitility.await
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Assert.assertThat
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

        mAppRule.getMockedDI().setAuthorized()
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
        mWebServer.setDispatcher(RequestDispatcher())

        mAppRule.launch(MountsFragment::class.java, MountsFragmentArgs(true))

        await().atMost(10, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(withId(R.id.crdlMounts)).check(matches(isDisplayed()))
                }
    }

    @Test
    fun updateMountsShowAll() {
        mWebServer.setDispatcher(RequestDispatcher())

        mAppRule.launch(MountsFragment::class.java, MountsFragmentArgs(true))

        await().atMost(10, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(withId(R.id.crdlMounts)).check(matches(isDisplayed()))
                }

        await().atMost(5, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(allOf(isDisplayed(), withId(R.id.rvMounts)))
                            .check(recyclerViewItemCountAssertion(4))
                }

        onView(withId(R.id.vpMounts)).perform(scrollRight())

        await().atMost(5, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(allOf(isDisplayed(), withId(R.id.rvMounts)))
                            .check(recyclerViewItemCountAssertion(3))
                }

        onView(withId(R.id.vpMounts)).perform(scrollRight())

        await().atMost(5, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(allOf(isDisplayed(), withId(R.id.rvMounts)))
                            .check(recyclerViewItemCountAssertion(2))
                }

        onView(withId(R.id.vpMounts)).perform(scrollRight())

        await().atMost(5, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(allOf(isDisplayed(), withId(R.id.rvMounts)))
                            .check(recyclerViewItemCountAssertion(3))
                }

        await().atMost(10, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(allOf(isDisplayed(), withId(R.id.rvMounts)))
                            .check(recyclerViewItemCountAssertion(451))
                }

        onView(withId(R.id.vpMounts)).perform(scrollLeft())

        await().atMost(5, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(allOf(isDisplayed(), withId(R.id.rvMounts)))
                            .check(recyclerViewItemCountAssertion(260))
                }

        onView(withId(R.id.vpMounts)).perform(scrollLeft())

        await().atMost(5, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(allOf(isDisplayed(), withId(R.id.rvMounts)))
                            .check(recyclerViewItemCountAssertion(702))
                }

        onView(withId(R.id.vpMounts)).perform(scrollLeft())

        await().atMost(5, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(allOf(isDisplayed(), withId(R.id.rvMounts)))
                            .check(recyclerViewItemCountAssertion(711))
                }
    }

    @Test
    fun updateMountsShowCollected() {
        mWebServer.setDispatcher(RequestDispatcher())

        mAppRule.launch(MountsFragment::class.java, MountsFragmentArgs(false))

        await().atMost(10, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(withId(R.id.crdlMounts)).check(matches(isDisplayed()))
                }

        await().atMost(5, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(allOf(isDisplayed(), withId(R.id.rvMounts)))
                            .check(recyclerViewItemCountAssertion(3))
                }

        onView(withId(R.id.vpMounts)).perform(scrollRight())

        await().atMost(5, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(allOf(isDisplayed(), withId(R.id.rvMounts)))
                            .check(recyclerViewItemCountAssertion(2))
                }

        onView(withId(R.id.vpMounts)).perform(scrollRight())

        await().atMost(5, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(allOf(isDisplayed(), withId(R.id.rvMounts)))
                            .check(recyclerViewItemCountAssertion(2))
                }

        onView(withId(R.id.vpMounts)).perform(scrollRight())

        await().atMost(5, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(allOf(isDisplayed(), withId(R.id.rvMounts)))
                            .check(recyclerViewItemCountAssertion(2))
                }

        await().atMost(10, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(allOf(isDisplayed(), withId(R.id.rvMounts)))
                            .check(recyclerViewItemCountAssertion(176))
                }

        onView(withId(R.id.vpMounts)).perform(scrollLeft())

        await().atMost(5, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(allOf(isDisplayed(), withId(R.id.rvMounts)))
                            .check(recyclerViewItemCountAssertion(101))
                }

        onView(withId(R.id.vpMounts)).perform(scrollLeft())

        await().atMost(5, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(allOf(isDisplayed(), withId(R.id.rvMounts)))
                            .check(recyclerViewItemCountAssertion(275))
                }

        onView(withId(R.id.vpMounts)).perform(scrollLeft())

        await().atMost(5, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(allOf(isDisplayed(), withId(R.id.rvMounts)))
                            .check(recyclerViewItemCountAssertion(277))
                }
    }

    @Test
    fun updateMountsError() {
        mWebServer.setDispatcher(ErrorDispatcher())

        mAppRule.launch(MountsFragment::class.java, MountsFragmentArgs(true))

        await().atMost(10, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(withId(R.id.crdlMounts)).check(matches(isDisplayed()))
                }

        await().atMost(5, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(withId(android.support.design.R.id.snackbar_text)).check(matches(isDisplayed()))
                }
    }

    @Test
    fun updateMountsError401() {
        mWebServer.setDispatcher(Error401Dispatcher())

        mAppRule.launch(MountsFragment::class.java, MountsFragmentArgs(true))

        await().atMost(10, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(withId(R.id.crdlMounts)).check(matches(isDisplayed()))
                }

        await().atMost(5, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(withId(android.support.design.R.id.snackbar_text)).check(matches(isDisplayed()))
                }

        await().atMost(15, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(withId(R.id.cnslSplash)).check(matches(isDisplayed()))
                }
    }

    @Test
    fun clickToAbout() {
        mWebServer.setDispatcher(RequestDispatcher())

        mAppRule.launch(MountsFragment::class.java, MountsFragmentArgs(true))

        await().atMost(10, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(withId(R.id.crdlMounts)).check(matches(isDisplayed()))
                }

        onView(withId(R.id.miAbout)).perform(click())

        await().atMost(15, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(withId(R.id.llAbout)).check(matches(isDisplayed()))
                }
    }

    @Test
    fun clickToFilter() {
        mWebServer.setDispatcher(RequestDispatcher())

        mAppRule.launch(MountsFragment::class.java, MountsFragmentArgs(true))

        await().atMost(10, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(withId(R.id.crdlMounts)).check(matches(isDisplayed()))
                }

        onView(withId(R.id.miFilter)).perform(click())

        await().atMost(15, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(withId(R.id.cnslFilter)).check(matches(isDisplayed()))
                }
    }

    @Test
    fun clickToLogout() {
        mWebServer.setDispatcher(RequestDispatcher())

        mAppRule.launch(MountsFragment::class.java, MountsFragmentArgs(true))

        await().atMost(10, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(withId(R.id.crdlMounts)).check(matches(isDisplayed()))
                }

        onView(withId(R.id.miLogout)).perform(click())

        await().atMost(15, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(withId(R.id.cnslAuthorization)).check(matches(isDisplayed()))
                }
    }

    @Test
    fun clickToMountItem() {
        mWebServer.setDispatcher(RequestDispatcher())

        mAppRule.launch(MountsFragment::class.java, MountsFragmentArgs(true))

        await().atMost(10, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(withId(R.id.crdlMounts)).check(matches(isDisplayed()))
                }

        await().atMost(10, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(allOf(isDisplayed(), withId(R.id.rvMounts)))
                            .check(recyclerViewItemCountAssertion(711))
                }

        onView(allOf(isDisplayed(), withId(R.id.rvMounts)))
                .perform(RecyclerViewActions.actionOnItemAtPosition<ItemsAdapter.ViewHolder>(0, click()))

        await().atMost(15, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(withId(R.id.llMount)).check(matches(isDisplayed()))
                }

        await().atMost(10, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(withId(R.id.tvMountName)).check(matches(withText("\"Гнильватер\"")))
                }
    }


    private fun recyclerViewItemCountAssertion(expectedCount: Int) =
            ViewAssertion { view, noViewFoundException ->
                if (noViewFoundException != null) {
                    throw noViewFoundException
                }
                val recyclerView = view as RecyclerView
                val adapter = recyclerView.adapter
                assertThat(adapter!!.itemCount, `is`(expectedCount))
            }
}