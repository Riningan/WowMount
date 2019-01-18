package com.riningan.wowmount.presenter

import com.riningan.util.Logger
import com.riningan.wowmount.CHARACTER
import com.riningan.wowmount.MOUNT_LIST
import com.riningan.wowmount.rule.KodeinRule
import com.riningan.wowmount.rule.LogRule
import com.riningan.wowmount.setPrivateField
import com.riningan.wowmount.ui.splash.SplashPresenter
import io.mockk.every
import io.mockk.verifySequence
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class SplashPresenterTest {
    @get: Rule
    val mLogRule = LogRule()
    @get: Rule
    val mKodeinRule = KodeinRule()
    private lateinit var mSplashPresenter: SplashPresenter


    @Before
    fun setup() {
        Logger.Config.setEnabled(false)
        mSplashPresenter = SplashPresenter(mKodeinRule.kodein)
        mSplashPresenter
    }

    /**
     * not activated
     */
    @Test
    fun onStart_1() {
        setmClear(false)
        every { mKodeinRule.localPreferences.isActivated } returns false
        every { mKodeinRule.characterInteractor.update() } returns Observable.just(Pair(CHARACTER, MOUNT_LIST))
        mSplashPresenter
                .onStart()
        with(mKodeinRule) {
            verifySequence {
                localPreferences.isActivated
                characterInteractor.update()
                router.newRootScreen(any(), any())
            }
        }
    }


    private fun setmClear(value: Boolean) {
        setPrivateField(SplashPresenter::class, "mClear", mSplashPresenter, value)
    }
}