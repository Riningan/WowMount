package com.riningan.wowmount.presenter

import com.riningan.wowmount.presentation.ui.about.AboutPresenter
import com.riningan.wowmount.presentation.ui.about.AboutView
import com.riningan.wowmount.rule.KodeinMockRule
import com.riningan.wowmount.rule.LogRule
import com.riningan.wowmount.rule.LoggerDisableRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class AboutPresenterTest {
    @get: Rule
    val mLogRule = LogRule()
    @get: Rule
    val mLoggerRule = LoggerDisableRule()
    @get: Rule
    val mKodeinRule = KodeinMockRule()

    private val mViewState: AboutView = mockk(relaxed = true)
    private lateinit var mAboutPresenter: AboutPresenter


    @Before
    fun setup() {
        mAboutPresenter = spyk(AboutPresenter(mKodeinRule.kodein))
        every { mAboutPresenter.viewState } returns mViewState
    }


    @Test
    fun onBackClick() {
        mAboutPresenter.onBackClick()

        verifySequence {
            mKodeinRule.router.exit()
        }
    }
}