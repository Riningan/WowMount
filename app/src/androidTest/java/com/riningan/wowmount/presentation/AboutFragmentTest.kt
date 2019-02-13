package com.riningan.wowmount.presentation

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withContentDescription
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.runner.AndroidJUnit4
import com.riningan.wowmount.R
import com.riningan.wowmount.presentation.ui.about.AboutFragment
import com.riningan.wowmount.rule.AppRule
import org.awaitility.Awaitility.await
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class AboutFragmentTest {
    @get: Rule
    var mAppRule = AppRule()


    @Test
    fun checkContainer() {
        onView(withId(android.R.id.content)).check(matches(isDisplayed()))
    }

    @Test
    fun checkLayout() {
        mAppRule.getMockedDI().setAuthorized()
        mAppRule.launch(AboutFragment::class.java)

        await().atMost(10, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(withId(R.id.llAbout)).check(matches(isDisplayed()))
                }
    }

    @Test
    fun backClick() {
        mAppRule.getMockedDI().setAuthorized()
        mAppRule.launch(AboutFragment::class.java)

        await().atMost(10, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    onView(withId(R.id.llAbout)).check(matches(isDisplayed()))
                }

        onView(withContentDescription(R.string.about_back)).perform(click())

        await().atMost(15, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted {
                    assertTrue(mAppRule.getActivity().isDestroyed)
                }
    }
}