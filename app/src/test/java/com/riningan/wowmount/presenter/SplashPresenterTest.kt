package com.riningan.wowmount.presenter

import android.view.View
import com.riningan.wowmount.CHARACTER
import com.riningan.wowmount.MOUNT_LIST
import com.riningan.wowmount.rule.KodeinRule
import com.riningan.wowmount.rule.LogRule
import com.riningan.wowmount.rule.LoggerRule
import com.riningan.wowmount.rule.RxRule
import com.riningan.wowmount.setPrivateField
import com.riningan.wowmount.ui.splash.SplashPresenter
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class SplashPresenterTest {
    @get: Rule
    val mLogRule = LogRule()
    @get: Rule
    val mRxRule = RxRule()
    @get: Rule
    val mKodeinRule = KodeinRule()
    @get: Rule
    val mLoggerRule = LoggerRule()

    private val mViewState: View = mockk(relaxed = true)
    private lateinit var mSplashPresenter: SplashPresenter


    @Before
    fun setup() {
        mSplashPresenter = SplashPresenter(mKodeinRule.kodein)
        setPrivateField(SplashPresenter::class, "mViewStateAsView", mSplashPresenter, mViewState)
    }

    /**
     * not activated
     */
    @Test
    fun onStart_1() {
        setmClear(false)
        every { mKodeinRule.localPreferences.isActivated } returns false
        every { mKodeinRule.characterInteractor.update() } returns Observable.just(Pair(CHARACTER, MOUNT_LIST))

        mSplashPresenter.onStart()

        (mKodeinRule.schedulersProvider.executorThread() as TestScheduler).triggerActions()

        with(mKodeinRule) {
            verifySequence {
                localPreferences.isActivated
                router.navigateTo(any())
            }
        }
    }


    private fun setmClear(value: Boolean) {
        setPrivateField(SplashPresenter::class, "mClear", mSplashPresenter, value)
    }
}