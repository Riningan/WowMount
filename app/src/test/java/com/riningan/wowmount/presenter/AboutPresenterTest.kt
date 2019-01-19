package com.riningan.wowmount.presenter

import com.riningan.wowmount.rule.KodeinRule
import com.riningan.wowmount.rule.LogRule
import com.riningan.wowmount.rule.LoggerRule
import com.riningan.wowmount.ui.about.AboutPresenter
import com.riningan.wowmount.ui.about.AboutView
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
    val mLoggerRule = LoggerRule()
    @get: Rule
    val mKodeinRule = KodeinRule()

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

        with(mKodeinRule) {
            verifySequence {
                router.exit()
            }
        }
    }
}