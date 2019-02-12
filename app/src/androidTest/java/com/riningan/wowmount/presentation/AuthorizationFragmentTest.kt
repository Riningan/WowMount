package com.riningan.wowmount.presentation

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.replaceText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.widget.ProgressBar
import com.riningan.wowmount.NAME
import com.riningan.wowmount.R
import com.riningan.wowmount.REALM
import com.riningan.wowmount.REGION
import com.riningan.wowmount.dispatcher.Error404Dispatcher
import com.riningan.wowmount.dispatcher.RequestDispatcher
import com.riningan.wowmount.presentation.ui.authorization.AuthorizationFragment
import com.riningan.wowmount.rule.AppRule
import com.riningan.wowmount.rule.KodeinDIMockRule
import com.riningan.wowmount.test.TestActivity
import okhttp3.mockwebserver.MockWebServer
import org.awaitility.Awaitility.await
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class AuthorizationFragmentTest {
    @get: Rule
    var mAppRule = AppRule()
//    @get: Rule
//    val mKodeinDIMockRule = KodeinDIMockRule()
//    @get: Rule
//    val mActivityTestRule = ActivityTestRule(TestActivity::class.java, true, true)

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


    @Test
    fun authorization() {
        mWebServer.setDispatcher(RequestDispatcher(true))

        mAppRule.launch(AuthorizationFragment::class.java)
//        mActivityTestRule.activity.setFragment(AuthorizationFragment::class.java)

        await().atMost(10, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted { onView(withId(R.id.cnslAuthorization)).check(matches(isDisplayed())) }

        disableAnimation()

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

        await().atMost(100, TimeUnit.SECONDS)
                .ignoreExceptions()
                .untilAsserted { onView(withId(R.id.crdlMounts)).check(matches(isDisplayed())) }
    }

//    @Test
//    fun wrongAuthorisation() {
//        mWebServer.setDispatcher(Error404Dispatcher())
//
//        mAppRule.launch(AuthorizationFragment::class.java)
//
//        await().atMost(10, TimeUnit.SECONDS)
//                .ignoreExceptions()
//                .untilAsserted { onView(withId(R.id.cnslAuthorization)).check(matches(isDisplayed())) }
//
//        disableAnimation()
//
//        onView(withId(R.id.cmbAuthorizationRegion)).perform(click())
//        onData(allOf(`is`(instanceOf(String::class.java)), `is`(REGION))).perform(click())
//        onView(withId(R.id.cmbAuthorizationRegion)).check(matches(withSpinnerText(containsString(REGION))))
//        Thread.sleep(1000)
//
//        onView(withId(R.id.etAuthorizationRealm)).perform(replaceText(REALM))
//        Thread.sleep(1000)
//        onView(withId(R.id.etAuthorizationCharacter)).perform(replaceText("Wrong Name"))
//        Thread.sleep(1000)
//
//        onView(withId(R.id.btnAuthorizationShow)).perform(click())
//
//        onView(withId(R.id.pbAuthorization)).check(matches(isDisplayed()))
//
//        await().atMost(10, TimeUnit.SECONDS)
//                .ignoreExceptions()
//                .untilAsserted { onView(withId(android.support.design.R.id.snackbar_text)).check(matches(isDisplayed())) }
//
//        await().atMost(10, TimeUnit.SECONDS)
//                .ignoreExceptions()
//                .untilAsserted { onView(withId(R.id.cnslAuthorization)).check(matches(isDisplayed())) }
//    }


    private fun disableAnimation() {
        // this is not error - ic_test_progress
        val notAnimatedDrawable = InstrumentationRegistry.getContext().getDrawable(com.riningan.wowmount.test.R.drawable.ic_test_progress)
        (mAppRule.getActivity().findViewById(R.id.pbAuthorization) as ProgressBar).indeterminateDrawable = notAnimatedDrawable
//        (mActivityTestRule.getActivity().findViewById(R.id.pbAuthorization) as ProgressBar).indeterminateDrawable = notAnimatedDrawable
    }
}