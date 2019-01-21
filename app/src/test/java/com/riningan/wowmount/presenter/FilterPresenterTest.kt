package com.riningan.wowmount.presenter

import com.riningan.wowmount.CHARACTER
import com.riningan.wowmount.CHARACTER_COLLECTED_MOUNT_LIST
import com.riningan.wowmount.MOUNT_LIST
import com.riningan.wowmount.interactor.WowMountExceptions
import com.riningan.wowmount.rule.*
import com.riningan.wowmount.setPrivateField
import com.riningan.wowmount.ui.filter.FilterPresenter
import com.riningan.wowmount.ui.filter.FilterView
import com.riningan.wowmount.ui.mounts.MountsFragment
import com.riningan.wowmount.ui.splash.SplashFragment
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FilterPresenterTest {
    @get: Rule
    val mLogRule = LogRule()
    @get: Rule
    val mLoggerRule = LoggerDisableRule()
    @get: Rule
    val mKodeinRule = KodeinMockRule()
    @get: Rule
    val mRxRule = RxRule()
    @get: Rule
    val mWowMountExceptionsRule = WowMountExceptionsMockRule()

    private val mViewState: FilterView = mockk(relaxed = true)
    private lateinit var mFilterPresenter: FilterPresenter


    @Before
    fun setup() {
        mFilterPresenter = spyk(FilterPresenter(mKodeinRule.kodein))
        every { mFilterPresenter.viewState } returns mViewState
    }


    @Test
    fun onViewCreated() {
        mFilterPresenter.onViewCreated()

        verifySequence {
            mViewState.setFilter(any())
        }
    }

    /**
     * Normal case
     * mShowAll == true
     */
    @Test
    fun onStart_1() {
        setPrivateField(FilterPresenter::class, "mShowAll", mFilterPresenter, true)
        every { mKodeinRule.characterInteractor.get() } returns Observable.just(Pair(CHARACTER, MOUNT_LIST))

        mFilterPresenter.onStart()

        verifySequence {
            mViewState.showProgress()
            mKodeinRule.characterInteractor.get()
            mViewState.showButton(MOUNT_LIST.size)
        }
    }

    /**
     * Normal case
     * mShowAll == false
     */
    @Test
    fun onStart_2() {
        setPrivateField(FilterPresenter::class, "mShowAll", mFilterPresenter, false)
        every { mKodeinRule.characterInteractor.get() } returns Observable.just(Pair(CHARACTER, MOUNT_LIST))

        mFilterPresenter.onStart()

        verifySequence {
            mViewState.showProgress()
            mKodeinRule.characterInteractor.get()
            mViewState.showButton(CHARACTER_COLLECTED_MOUNT_LIST.size)
        }
    }

    /**
     * IOException
     */
    @Test
    fun onStart_3() {
        setPrivateField(FilterPresenter::class, "mShowAll", mFilterPresenter, true)
        every { mKodeinRule.characterInteractor.get() } returns Observable.error(WowMountExceptions.IOException())

        mFilterPresenter.onStart()

        verifySequence {
            mViewState.showProgress()
            mKodeinRule.characterInteractor.get()
            mViewState.showError(any())
        }
    }

    /**
     * AuthorizedException
     */
    @Test
    fun onStart_4() {
        setPrivateField(FilterPresenter::class, "mShowAll", mFilterPresenter, true)
        every { mKodeinRule.characterInteractor.get() } returns Observable.error(WowMountExceptions.AuthorizedException())

        mFilterPresenter.onStart()

        verifySequence {
            mViewState.showProgress()
            mKodeinRule.characterInteractor.get()
            mViewState.showError(any())
            mKodeinRule.router.newRootScreen(SplashFragment::class.java.canonicalName, any())
        }
    }

    @Test
    fun onBackClick() {
        mFilterPresenter.onBackClick()

        verifySequence {
            mKodeinRule.router.exit()
        }
    }

    @Test
    fun onShowAllChecked() {
        setPrivateField(FilterPresenter::class, "mShowAll", mFilterPresenter, true)
        every { mKodeinRule.characterInteractor.get() } returns Observable.just(Pair(CHARACTER, MOUNT_LIST))

        // call it before for create subject
        mFilterPresenter.onStart()
        mFilterPresenter.onShowAllChecked(false)

        verifySequence {
            // onStart
            mViewState.showProgress()
            mKodeinRule.characterInteractor.get()
            mViewState.showButton(MOUNT_LIST.size)
            // onShowAllChecked
            mViewState.showProgress()
            mKodeinRule.characterInteractor.get()
            mViewState.showButton(CHARACTER_COLLECTED_MOUNT_LIST.size)
        }
    }

    @Test
    fun onShowClick() {
        every { mKodeinRule.localPreferences.showAll = any() } returns Unit

        mFilterPresenter.onShowClick()

        verifySequence {
            mKodeinRule.localPreferences.showAll = any()
            mKodeinRule.router.newRootScreen(MountsFragment::class.java.canonicalName, any())
        }
    }
}