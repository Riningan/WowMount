package com.riningan.wowmount

import com.riningan.wowmount.ui.splash.SplashPresenter
import org.junit.Before
import org.junit.Rule


class SplashPresenterTest {
    @get: Rule
    val mLogRule = LogRule()
    private lateinit var mSplashPresenter: SplashPresenter

    @Before
    fun onStart() {
        mSplashPresenter = SplashPresenter()
    }
}