package com.riningan.wowmount.presentation

import android.R
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import com.riningan.wowmount.rule.AppRule
import org.awaitility.Awaitility.await
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit


abstract class BaseTest {
    @get: Rule
    var mAppRule = AppRule()


    @Test
    fun checkContainer() {
        onView(withId(R.id.content)).check(matches(isDisplayed()))
    }

    fun waitCheckFragmentIsDisplayed(viewId: Int) {
        await().atMost(15, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(withId(viewId)).check(matches(isDisplayed()))
                }
    }

    fun waitCheckSnackbarIsDisplayed() {
        await().atMost(5, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(withId(android.support.design.R.id.snackbar_text)).check(matches(isDisplayed()))
                }
    }

    fun waitCheckSnackbarText(text: String) {
        await().atMost(5, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(withId(android.support.design.R.id.snackbar_text))
                            .check(matches(withText(text)))
                }
    }

    fun waitCheckExit() {
        await().atMost(15, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    assertTrue(mAppRule.getActivity().isDestroyed)
                }
    }
}