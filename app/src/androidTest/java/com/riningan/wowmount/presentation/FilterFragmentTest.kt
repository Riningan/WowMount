package com.riningan.wowmount.presentation

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ViewActions.actionWithAssertions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom
import android.support.test.espresso.matcher.ViewMatchers.isChecked
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.widget.ProgressBar
import com.riningan.frarg.processor.FilterFragmentArgs
import com.riningan.wowmount.R
import com.riningan.wowmount.dispatcher.RequestDispatcher
import com.riningan.wowmount.presentation.ui.filter.FilterFragment
import com.riningan.wowmount.rule.AppRule
import okhttp3.mockwebserver.MockWebServer
import org.awaitility.Awaitility.await
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class FilterFragmentTest {
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
        mAppRule.launch(FilterFragment::class.java, FilterFragmentArgs(false))

        await().atMost(10, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(withId(R.id.cnslFilter)).check(matches(isDisplayed()))
                }
    }

    @Test
    fun checkShow() {
        mWebServer.setDispatcher(RequestDispatcher())

        mAppRule.launch(FilterFragment::class.java, FilterFragmentArgs(false))

        onView(isAssignableFrom(ProgressBar::class.java)).perform(replaceProgressBarDrawable())

        await().atMost(10, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(withId(R.id.cnslFilter)).check(matches(isDisplayed()))
                }

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


    private fun replaceProgressBarDrawable(): ViewAction {
        return actionWithAssertions(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(ProgressBar::class.java)
            }

            override fun getDescription(): String {
                return "replace the ProgressBar drawable"
            }

            override fun perform(uiController: UiController, view: View) {
                // Replace the indeterminate drawable with a static red ColorDrawable
                val progressBar = view as ProgressBar
                val notAnimatedDrawable = InstrumentationRegistry.getContext().getDrawable(com.riningan.wowmount.test.R.drawable.ic_test_progress)
                progressBar.indeterminateDrawable = notAnimatedDrawable
                uiController.loopMainThreadUntilIdle()
            }
        })
    }
}