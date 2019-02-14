package com.riningan.wowmount.presentation

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.ViewAssertion
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.contrib.ViewPagerActions.scrollLeft
import android.support.test.espresso.contrib.ViewPagerActions.scrollRight
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.runner.AndroidJUnit4
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import com.riningan.frarg.processor.MountsFragmentArgs
import com.riningan.wowmount.R
import com.riningan.wowmount.dispatcher.Error401Dispatcher
import com.riningan.wowmount.dispatcher.ErrorDispatcher
import com.riningan.wowmount.dispatcher.RequestDispatcher
import com.riningan.wowmount.getPrivateField
import com.riningan.wowmount.presentation.ui.mounts.ItemsAdapter
import com.riningan.wowmount.presentation.ui.mounts.MountsFragment
import okhttp3.mockwebserver.MockWebServer
import org.awaitility.Awaitility.await
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class MountsFragmentTest : BaseTest() {
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

        mAppRule.launch(MountsFragment::class.java, MountsFragmentArgs(true))

        waitCheckFragmentIsDisplayed(R.id.crdlMounts)
    }

    @Test
    fun updateMountsShowAll() {
        mWebServer.setDispatcher(RequestDispatcher())

        mAppRule.launch(MountsFragment::class.java, MountsFragmentArgs(true))

        waitCheckFragmentIsDisplayed(R.id.crdlMounts)
        waitCheckCurrentRecyclerViewItemCount(4)
        onView(withId(R.id.vpMounts)).perform(scrollRight())
        waitCheckCurrentRecyclerViewItemCount(3)
        onView(withId(R.id.vpMounts)).perform(scrollRight())
        waitCheckCurrentRecyclerViewItemCount(2)
        onView(withId(R.id.vpMounts)).perform(scrollRight())
        waitCheckCurrentRecyclerViewItemCount(3)
        waitCheckCurrentRecyclerViewItemCount(451)
        onView(withId(R.id.vpMounts)).perform(scrollLeft())
        waitCheckCurrentRecyclerViewItemCount(260)
        onView(withId(R.id.vpMounts)).perform(scrollLeft())
        waitCheckCurrentRecyclerViewItemCount(702)
        onView(withId(R.id.vpMounts)).perform(scrollLeft())
        waitCheckCurrentRecyclerViewItemCount(711)
    }

    @Test
    fun updateMountsShowCollected() {
        mWebServer.setDispatcher(RequestDispatcher())

        mAppRule.launch(MountsFragment::class.java, MountsFragmentArgs(false))

        waitCheckFragmentIsDisplayed(R.id.crdlMounts)
        waitCheckCurrentRecyclerViewItemCount(3)
        onView(withId(R.id.vpMounts)).perform(scrollRight())
        waitCheckCurrentRecyclerViewItemCount(2)
        onView(withId(R.id.vpMounts)).perform(scrollRight())
        waitCheckCurrentRecyclerViewItemCount(2)
        onView(withId(R.id.vpMounts)).perform(scrollRight())
        waitCheckCurrentRecyclerViewItemCount(2)
        waitCheckCurrentRecyclerViewItemCount(176)
        onView(withId(R.id.vpMounts)).perform(scrollLeft())
        waitCheckCurrentRecyclerViewItemCount(101)
        onView(withId(R.id.vpMounts)).perform(scrollLeft())
        waitCheckCurrentRecyclerViewItemCount(275)
        onView(withId(R.id.vpMounts)).perform(scrollLeft())
        waitCheckCurrentRecyclerViewItemCount(277)
    }

    @Test
    fun updateMountsError() {
        mWebServer.setDispatcher(ErrorDispatcher())

        mAppRule.launch(MountsFragment::class.java, MountsFragmentArgs(true))

        waitCheckFragmentIsDisplayed(R.id.crdlMounts)
        waitCheckSnackbarIsDisplayed()
    }

    @Test
    fun updateMountsError401() {
        mWebServer.setDispatcher(Error401Dispatcher())

        mAppRule.launch(MountsFragment::class.java, MountsFragmentArgs(true))

        waitCheckFragmentIsDisplayed(R.id.crdlMounts)
        waitCheckSnackbarIsDisplayed()
        waitCheckFragmentIsDisplayed(R.id.cnslSplash)
    }

    @Test
    fun clickToAbout() {
        mWebServer.setDispatcher(RequestDispatcher())

        mAppRule.launch(MountsFragment::class.java, MountsFragmentArgs(true))

        waitCheckFragmentIsDisplayed(R.id.crdlMounts)
        onView(withId(R.id.miAbout)).perform(click())
        waitCheckFragmentIsDisplayed(R.id.llAbout)
    }

    @Test
    fun clickToFilter() {
        mWebServer.setDispatcher(RequestDispatcher())

        mAppRule.launch(MountsFragment::class.java, MountsFragmentArgs(true))

        waitCheckFragmentIsDisplayed(R.id.crdlMounts)
        onView(withId(R.id.miFilter)).perform(click())
        waitCheckFragmentIsDisplayed(R.id.cnslFilter)
    }

    @Test
    fun clickToLogout() {
        mWebServer.setDispatcher(RequestDispatcher())

        mAppRule.launch(MountsFragment::class.java, MountsFragmentArgs(true))

        waitCheckFragmentIsDisplayed(R.id.crdlMounts)
        onView(withId(R.id.miLogout)).perform(click())
        waitCheckFragmentIsDisplayed(R.id.cnslAuthorization)
    }

    @Test
    fun clickToMountItem() {
        mWebServer.setDispatcher(RequestDispatcher())

        mAppRule.launch(MountsFragment::class.java, MountsFragmentArgs(true))

        waitCheckFragmentIsDisplayed(R.id.crdlMounts)
        waitCheckCurrentRecyclerViewItemCount(711)
        onView(allOf(isDisplayed(), withId(R.id.rvMounts)))
                .perform(RecyclerViewActions.actionOnItemAtPosition<ItemsAdapter.ViewHolder>(0, click()))
        waitCheckFragmentIsDisplayed(R.id.llMount)
        await().atMost(10, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(withId(R.id.tvMountName)).check(matches(withText("\"Гнильватер\"")))
                }
    }

    @Test
    fun swipeRefresh() {
        mWebServer.setDispatcher(ErrorDispatcher())

        mAppRule.launch(MountsFragment::class.java, MountsFragmentArgs(true))

        waitCheckFragmentIsDisplayed(R.id.crdlMounts)
        waitCheckCurrentRecyclerViewItemCount(4)
        waitCheckSnackbarIsDisplayed()
        mWebServer.setDispatcher(RequestDispatcher())
        onView(withId(R.id.srlMounts)).perform(setRefreshing())
        waitCheckCurrentRecyclerViewItemCount(711)
    }


    private fun waitCheckCurrentRecyclerViewItemCount(expectedCount: Int) {
        await().atMost(10, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(allOf(isDisplayed(), withId(R.id.rvMounts)))
                            .check(recyclerViewItemCountAssertion(expectedCount))
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

    private fun setRefreshing() = object : ViewAction {
        override fun getConstraints(): Matcher<View> = isAssignableFrom(SwipeRefreshLayout::class.java)

        override fun getDescription(): String = "Set SwipeRefreshLayout refreshing state"

        override fun perform(uiController: UiController, view: View) {
            uiController.loopMainThreadUntilIdle()
            val swipeRefreshLayout = view as SwipeRefreshLayout
            getPrivateField<SwipeRefreshLayout, SwipeRefreshLayout.OnRefreshListener>(SwipeRefreshLayout::class, "mListener", swipeRefreshLayout) {
                it.onRefresh()
            }
        }
    }
}