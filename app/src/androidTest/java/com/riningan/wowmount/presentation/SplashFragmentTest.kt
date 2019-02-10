package com.riningan.wowmount.presentation

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.runner.AndroidJUnit4
import com.riningan.frarg.processor.SplashFragmentArgs
import com.riningan.wowmount.NAME
import com.riningan.wowmount.R
import com.riningan.wowmount.REALM
import com.riningan.wowmount.REGION
import com.riningan.wowmount.dispatcher.Error401Dispatcher
import com.riningan.wowmount.dispatcher.ErrorDispatcher
import com.riningan.wowmount.dispatcher.RequestDispatcher
import com.riningan.wowmount.presentation.ui.splash.SplashFragment
import com.riningan.wowmount.rule.AppRule
import com.riningan.wowmount.rule.LogRule
import com.riningan.wowmount.rule.LoggerDisableRule
import okhttp3.mockwebserver.MockWebServer
import org.awaitility.Awaitility.await
import org.junit.After
import org.junit.Before
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
    fun checkContainerIsDisplayed() {
        onView(withId(android.R.id.content)).check(matches(isDisplayed()))
    }

    /**
     * user is not authorized
     *
     * go to AuthorizationFragment
     */
    @Test
    fun routeToAuthorisation() {
        mAppRule.getMockedLocalPreferences().isActivated = false

        mAppRule.launch(SplashFragment::class.java)

        await().atMost(10, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted { onView(withId(R.id.cnslAuthorization)).check(matches(isDisplayed())) }
    }

    /**
     * user is authorized
     *
     * go to MountsFragment
     */
    @Test
    fun routeToMounts() {
        mAppRule.getMockedLocalPreferences().isActivated = true
        mAppRule.getMockedLocalPreferences().server = REGION
        mAppRule.getMockedLocalPreferences().realmName = REALM
        mAppRule.getMockedLocalPreferences().characterName = NAME
        mWebServer.setDispatcher(RequestDispatcher())

        mAppRule.launch(SplashFragment::class.java)

        await().atMost(10, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted { onView(withId(R.id.crdlMounts)).check(matches(isDisplayed())) }
    }

    /**
     * user is authorized
     * but returned error
     *
     * showError
     */
    @Test
    fun routeToMountsButError() {
        mAppRule.getMockedLocalPreferences().isActivated = true
        mAppRule.getMockedLocalPreferences().server = REGION
        mAppRule.getMockedLocalPreferences().realmName = REALM
        mAppRule.getMockedLocalPreferences().characterName = NAME
        mWebServer.setDispatcher(ErrorDispatcher())

        mAppRule.launch(SplashFragment::class.java)

        await().atMost(10, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted { onView(withId(android.support.design.R.id.snackbar_text)).check(matches(isDisplayed())) }

        await().atMost(1, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted { onView(withId(R.id.cnslSplash)).check(matches(isDisplayed())) }
    }

    /**
     * user is authorized
     * but returned 401 error
     * and need authorize again
     *
     * go to AuthorizationFragment
     */
    @Test
    fun routeToMountsButError401() {
        mAppRule.getMockedLocalPreferences().isActivated = true
        mAppRule.getMockedLocalPreferences().server = REGION
        mAppRule.getMockedLocalPreferences().realmName = REALM
        mAppRule.getMockedLocalPreferences().characterName = NAME
        mWebServer.setDispatcher(Error401Dispatcher())

        mAppRule.launch(SplashFragment::class.java)

        await().atMost(15, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted { onView(withId(R.id.cnslAuthorization)).check(matches(isDisplayed())) }
    }

    /**
     * user is authorized
     * somewhere was 401 error
     *
     * logout
     * go to AuthorizationFragment
     */
    @Test
    fun logout() {
        mAppRule.getMockedLocalPreferences().isActivated = true
        mAppRule.getMockedLocalPreferences().server = REGION
        mAppRule.getMockedLocalPreferences().realmName = REALM
        mAppRule.getMockedLocalPreferences().characterName = NAME

        mAppRule.launch(SplashFragment::class.java, SplashFragmentArgs(true))

        await().atMost(10, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted { onView(withId(R.id.cnslAuthorization)).check(matches(isDisplayed())) }
    }
}
