package com.riningan.wowmount.presenter

import com.riningan.wowmount.CHARACTER
import com.riningan.wowmount.MOUNT_LIST
import com.riningan.wowmount.interactor.WowMountExceptions
import com.riningan.wowmount.rule.KodeinRule
import com.riningan.wowmount.rule.LogRule
import com.riningan.wowmount.rule.LoggerRule
import com.riningan.wowmount.rule.RxRule
import com.riningan.wowmount.rule.WowMountExceptionsRule
import com.riningan.wowmount.setPrivateField
import com.riningan.wowmount.ui.authorization.AuthorizationFragment
import com.riningan.wowmount.ui.mounts.MountsFragment
import com.riningan.wowmount.ui.splash.SplashFragment
import com.riningan.wowmount.ui.splash.SplashPresenter
import com.riningan.wowmount.ui.splash.SplashView
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class SplashPresenterTest {
    @get: Rule
    val mLogRule = LogRule()
    @get: Rule
    val mLoggerRule = LoggerRule()
    @get: Rule
    val mRxRule = RxRule()
    @get: Rule
    val mKodeinRule = KodeinRule()
    @get: Rule
    val mWowMountExceptionsRule = WowMountExceptionsRule()

    private val mViewState: SplashView = mockk(relaxed = true)
    private lateinit var mSplashPresenter: SplashPresenter


    @Before
    fun setup() {
        mSplashPresenter = spyk(SplashPresenter(mKodeinRule.kodein))
        every { mSplashPresenter.viewState } returns mViewState
    }


    /**
     * Not activated
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
                router.navigateTo(AuthorizationFragment::class.java.canonicalName)
            }
        }
    }

    /**
     * Activated
     * Success data
     */
    @Test
    fun onStart_2() {
        setmClear(false)
        every { mKodeinRule.localPreferences.isActivated } returns true
        every { mKodeinRule.localPreferences.showAll } returns true
        every { mKodeinRule.characterInteractor.update() } returns Observable.just(Pair(CHARACTER, MOUNT_LIST))

        mSplashPresenter.onStart()

        (mKodeinRule.schedulersProvider.executorThread() as TestScheduler).triggerActions()

        with(mKodeinRule) {
            verifySequence {
                localPreferences.isActivated
                characterInteractor.update()
                localPreferences.showAll
                router.newRootScreen(MountsFragment::class.java.canonicalName, any())
            }
        }
    }

    /**
     * Activated
     * Fail data
     */
    @Test
    fun onStart_3() {
        setmClear(false)
        every { mKodeinRule.localPreferences.isActivated } returns true
        every { mKodeinRule.characterInteractor.update() } returns Observable.error(WowMountExceptions.IOException())

        mSplashPresenter.onStart()

        (mKodeinRule.schedulersProvider.executorThread() as TestScheduler).triggerActions()

        verifySequence {
            mKodeinRule.localPreferences.isActivated
            mKodeinRule.characterInteractor.update()
            mViewState.showErrorDialog(any())
        }
    }

    /**
     * Activated
     * AuthorizedException
     */
    @Test
    fun onStart_4() {
        setmClear(false)
        every { mKodeinRule.localPreferences.isActivated } returns true
        every { mKodeinRule.characterInteractor.update() } returns Observable.error(WowMountExceptions.AuthorizedException())
        every { mKodeinRule.characterInteractor.clear() } returns Completable.complete()
        every { mKodeinRule.localPreferences.clear() } returns Unit

        mSplashPresenter.onStart()

        (mKodeinRule.schedulersProvider.executorThread() as TestScheduler).triggerActions()

        with(mKodeinRule) {
            verifySequence {
                localPreferences.isActivated
                characterInteractor.update()
                mViewState.showErrorDialog(any())
//                mSplashPresenter["logout"]()
                characterInteractor.clear()
                localPreferences.clear()
                router.newRootScreen(SplashFragment::class.java.canonicalName)
            }
        }
    }

    /**
     * mClear == true
     */
    @Test
    fun onStart_5() {
        setmClear(true)
        every { mKodeinRule.characterInteractor.clear() } returns Completable.complete()
        every { mKodeinRule.localPreferences.clear() } returns Unit

        mSplashPresenter.onStart()

        (mKodeinRule.schedulersProvider.executorThread() as TestScheduler).triggerActions()

        with(mKodeinRule) {
            verifySequence {
                //                mSplashPresenter["logout"]()
                characterInteractor.clear()
                localPreferences.clear()
                router.newRootScreen(SplashFragment::class.java.canonicalName)
            }
        }
    }


    private fun setmClear(value: Boolean) {
        setPrivateField(SplashPresenter::class, "mClear", mSplashPresenter, value)
    }
}