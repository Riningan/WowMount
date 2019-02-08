package com.riningan.wowmount.presentation.ui

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.runner.AndroidJUnit4
import com.riningan.wowmount.NAME
import com.riningan.wowmount.R
import com.riningan.wowmount.REALM
import com.riningan.wowmount.REGION
import com.riningan.wowmount.presentation.ui.splash.SplashFragment
import com.riningan.wowmount.rule.AppRule
import com.riningan.wowmount.rule.LogRule
import com.riningan.wowmount.rule.LoggerDisableRule
import io.mockk.every
import org.awaitility.Awaitility.await
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class SplashFragmentTest {
    @get: Rule
    val mLogRule = LogRule()
    @get: Rule
    val mLoggerRule = LoggerDisableRule()
    @get: Rule
    var mAppRule = AppRule()


    @Test
    fun checkContainerIsDisplayed() {
        onView(withId(android.R.id.content)).check(matches(isDisplayed()))
    }

    @Test
    fun routeToAuthorisation() {
        every { mAppRule.getMockedLocalPreferences().isActivated } returns false

        mAppRule.launch(SplashFragment::class.java)

        await().atMost(10, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted { onView(withId(R.id.ivAuthorizationLogo)).check(matches(isDisplayed())) }
    }

    @Test
    fun routeToMounts() {
        every { mAppRule.getMockedLocalPreferences().isActivated } returns true
        every { mAppRule.getMockedLocalPreferences().server } returns REGION
        every { mAppRule.getMockedLocalPreferences().realmName } returns REALM
        every { mAppRule.getMockedLocalPreferences().characterName } returns NAME

        mAppRule.launch(SplashFragment::class.java)

        await().atMost(10, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted { onView(withId(R.id.ivAuthorizationLogo)).check(matches(isDisplayed())) }
    }
}
