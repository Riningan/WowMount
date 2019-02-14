package com.riningan.wowmount.presentation

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.runner.AndroidJUnit4
import com.riningan.frarg.processor.SplashFragmentArgs
import com.riningan.wowmount.R
import com.riningan.wowmount.dispatcher.Error401Dispatcher
import com.riningan.wowmount.dispatcher.ErrorDispatcher
import com.riningan.wowmount.dispatcher.RequestDispatcher
import com.riningan.wowmount.presentation.ui.splash.SplashFragment
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SplashFragmentTest : BaseTest() {
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
    fun checkLayout() {
        mAppRule.launch(SplashFragment::class.java)

        waitCheckFragmentIsDisplayed(R.id.cnslSplash)
    }

    /**
     * user is not authorized
     *
     * go to AuthorizationFragment
     */
    @Test
    fun routeToAuthorisation() {
        mAppRule.launch(SplashFragment::class.java)

        waitCheckFragmentIsDisplayed(R.id.cnslSplash)
        waitCheckFragmentIsDisplayed(R.id.cnslAuthorization)
    }

    /**
     * user is authorized
     *
     * go to MountsFragment
     */
    @Test
    fun routeToMounts() {
        mAppRule.getMockedDI().setAuthorized()
        mWebServer.setDispatcher(RequestDispatcher())

        mAppRule.launch(SplashFragment::class.java)

        waitCheckFragmentIsDisplayed(R.id.cnslSplash)
        waitCheckFragmentIsDisplayed(R.id.crdlMounts)
    }

    /**
     * user is authorized
     * but returned error
     *
     * showError
     */
    @Test
    fun networkError() {
        mAppRule.getMockedDI().setAuthorized()
        mWebServer.setDispatcher(ErrorDispatcher())

        mAppRule.launch(SplashFragment::class.java)

        waitCheckFragmentIsDisplayed(R.id.cnslSplash)
        waitCheckSnackbarIsDisplayed()
        onView(withId(R.id.cnslSplash)).check(matches(isDisplayed()))
    }

    /**
     * user is authorized
     * but returned 401 error
     * and need authorize again
     *
     * go to AuthorizationFragment
     */
    @Test
    fun authorizationError() {
        mAppRule.getMockedDI().setAuthorized()
        mWebServer.setDispatcher(Error401Dispatcher())

        mAppRule.launch(SplashFragment::class.java)

        waitCheckFragmentIsDisplayed(R.id.cnslSplash)
        waitCheckFragmentIsDisplayed(R.id.cnslAuthorization)
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
        mAppRule.getMockedDI().setAuthorized()

        mAppRule.launch(SplashFragment::class.java, SplashFragmentArgs(true))

        waitCheckFragmentIsDisplayed(R.id.cnslSplash)
        waitCheckFragmentIsDisplayed(R.id.cnslAuthorization)
    }
}