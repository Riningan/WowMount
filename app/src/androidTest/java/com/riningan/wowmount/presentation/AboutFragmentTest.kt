package com.riningan.wowmount.presentation

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.withContentDescription
import android.support.test.runner.AndroidJUnit4
import com.riningan.wowmount.R
import com.riningan.wowmount.presentation.ui.about.AboutFragment
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AboutFragmentTest : BaseTest() {
    @Test
    fun checkLayout() {
        mAppRule.getMockedDI().setAuthorized()

        mAppRule.launch(AboutFragment::class.java)

        waitCheckFragmentIsDisplayed(R.id.llAbout)
    }

    @Test
    fun backClick() {
        mAppRule.getMockedDI().setAuthorized()

        mAppRule.launch(AboutFragment::class.java)

        waitCheckFragmentIsDisplayed(R.id.llAbout)
        onView(withContentDescription(R.string.about_back)).perform(click())
        waitCheckExit()
    }
}