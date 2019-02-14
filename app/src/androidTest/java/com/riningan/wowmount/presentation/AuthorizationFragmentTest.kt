package com.riningan.wowmount.presentation

import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.replaceText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withSpinnerText
import android.support.test.runner.AndroidJUnit4
import com.riningan.wowmount.NAME
import com.riningan.wowmount.R
import com.riningan.wowmount.REALM
import com.riningan.wowmount.REGION
import com.riningan.wowmount.dispatcher.Error404Dispatcher
import com.riningan.wowmount.dispatcher.RequestDispatcher
import com.riningan.wowmount.presentation.ui.authorization.AuthorizationFragment
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AuthorizationFragmentTest : BaseTest() {
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
        mAppRule.launch(AuthorizationFragment::class.java)

        waitCheckFragmentIsDisplayed(R.id.cnslAuthorization)
    }

    @Test
    fun noRealm() {
        mAppRule.launch(AuthorizationFragment::class.java)

        waitCheckFragmentIsDisplayed(R.id.cnslAuthorization)
        onView(withId(R.id.btnAuthorizationShow)).perform(click())
        waitCheckSnackbarText(mAppRule.getActivity().getString(R.string.authorization_error_empty_realm))
    }

    @Test
    fun noCharacter() {
        mAppRule.launch(AuthorizationFragment::class.java)

        waitCheckFragmentIsDisplayed(R.id.cnslAuthorization)
        onView(withId(R.id.etAuthorizationRealm)).perform(replaceText(REALM))
        Thread.sleep(1000)
        onView(withId(R.id.btnAuthorizationShow)).perform(click())
        waitCheckSnackbarText(mAppRule.getActivity().getString(R.string.authorization_error_empty_character_name))
    }

    @Test
    fun authorization() {
        mWebServer.setDispatcher(RequestDispatcher())

        mAppRule.launch(AuthorizationFragment::class.java)

        waitCheckFragmentIsDisplayed(R.id.cnslAuthorization)
        onView(withId(R.id.cmbAuthorizationRegion)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`(REGION))).perform(click())
        onView(withId(R.id.cmbAuthorizationRegion)).check(matches(withSpinnerText(containsString(REGION))))
        Thread.sleep(1000)
        onView(withId(R.id.etAuthorizationRealm)).perform(replaceText(REALM))
        Thread.sleep(1000)
        onView(withId(R.id.etAuthorizationCharacter)).perform(replaceText(NAME))
        Thread.sleep(1000)
        onView(withId(R.id.btnAuthorizationShow)).perform(click())
        onView(withId(R.id.pbAuthorization)).check(matches(isDisplayed()))
        onView(withId(R.id.btnAuthorizationShow)).check(matches(not(isDisplayed())))
        waitCheckFragmentIsDisplayed(R.id.crdlMounts)
    }

    @Test
    fun wrongAuthorisation() {
        mWebServer.setDispatcher(Error404Dispatcher())

        mAppRule.launch(AuthorizationFragment::class.java)

        waitCheckFragmentIsDisplayed(R.id.cnslAuthorization)
        onView(withId(R.id.cmbAuthorizationRegion)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`(REGION))).perform(click())
        onView(withId(R.id.cmbAuthorizationRegion)).check(matches(withSpinnerText(containsString(REGION))))
        Thread.sleep(1000)
        onView(withId(R.id.etAuthorizationRealm)).perform(replaceText(REALM))
        Thread.sleep(1000)
        onView(withId(R.id.etAuthorizationCharacter)).perform(replaceText("Wrong Name"))
        Thread.sleep(1000)
        onView(withId(R.id.btnAuthorizationShow)).perform(click())
        onView(withId(R.id.pbAuthorization)).check(matches(isDisplayed()))
        onView(withId(R.id.btnAuthorizationShow)).check(matches(not(isDisplayed())))
        waitCheckSnackbarIsDisplayed()
        onView(withId(R.id.cnslAuthorization)).check(matches(isDisplayed()))
    }
}