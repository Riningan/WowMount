package com.riningan.wowmount.presenter

import com.riningan.wowmount.CHARACTER
import com.riningan.wowmount.MOUNT_1
import com.riningan.wowmount.MOUNT_2
import com.riningan.wowmount.MOUNT_3
import com.riningan.wowmount.MOUNT_4
import com.riningan.wowmount.MOUNT_LIST
import com.riningan.wowmount.domain.WowMountExceptions
import com.riningan.wowmount.presentation.ui.about.AboutFragment
import com.riningan.wowmount.presentation.ui.authorization.AuthorizationFragment
import com.riningan.wowmount.presentation.ui.filter.FilterFragment
import com.riningan.wowmount.presentation.ui.mount.MountFragment
import com.riningan.wowmount.presentation.ui.mounts.MountsPresenter
import com.riningan.wowmount.presentation.ui.mounts.MountsView
import com.riningan.wowmount.presentation.ui.splash.SplashFragment
import com.riningan.wowmount.rule.KodeinMockRule
import com.riningan.wowmount.rule.LogRule
import com.riningan.wowmount.rule.LoggerDisableRule
import com.riningan.wowmount.rule.RxSchedulersRule
import com.riningan.wowmount.rule.WowMountExceptionsMockRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class MountsPresenterTest {
    @get: Rule
    val mLogRule = LogRule()
    @get: Rule
    val mLoggerRule = LoggerDisableRule()
    @get: Rule
    val mKodeinRule = KodeinMockRule()
    @get: Rule
    val mRxRule = RxSchedulersRule()
    @get: Rule
    val mWowMountExceptionsRule = WowMountExceptionsMockRule()

    private val mViewState: MountsView = mockk(relaxed = true)
    private lateinit var mMountsPresenter: MountsPresenter


    @Before
    fun setup() {
        mMountsPresenter = spyk(MountsPresenter(mKodeinRule.router, mKodeinRule.characterInteractor, mKodeinRule.localPreferences))
        every { mMountsPresenter.viewState } returns mViewState
    }


    /**
     * Normal case
     */
    @Test
    fun loadCharacter_1() {
        every { mKodeinRule.characterInteractor.get() } returns Flowable.just(Pair(CHARACTER, MOUNT_LIST))

        mMountsPresenter.loadCharacter()

        (mKodeinRule.schedulersProvider.executorThread() as TestScheduler).triggerActions()

        verifySequence {
            mKodeinRule.characterInteractor.get()
            mViewState.setCharacter(CHARACTER)
            mViewState.setMounts(MOUNT_LIST)
        }
    }

    /**
     * Inversed mount list
     */
    @Test
    fun loadCharacter_2() {
        every { mKodeinRule.characterInteractor.get() } returns Flowable.just(Pair(CHARACTER, listOf(MOUNT_4, MOUNT_3, MOUNT_2, MOUNT_1)))

        mMountsPresenter.loadCharacter()

        (mKodeinRule.schedulersProvider.executorThread() as TestScheduler).triggerActions()

        verifySequence {
            mKodeinRule.characterInteractor.get()
            mViewState.setCharacter(CHARACTER)
            mViewState.setMounts(MOUNT_LIST)
        }
    }

    /**
     * IOException
     */
    @Test
    fun loadCharacter_3() {
        every { mKodeinRule.characterInteractor.get() } returns Flowable.error(WowMountExceptions.IOException())

        mMountsPresenter.loadCharacter()

        (mKodeinRule.schedulersProvider.executorThread() as TestScheduler).triggerActions()

        verifySequence {
            mKodeinRule.characterInteractor.get()
            mViewState.showRequestErrorDialog(any())
        }
    }

    /**
     * AuthorizedException
     */
    @Test
    fun loadCharacter_4() {
        every { mKodeinRule.characterInteractor.get() } returns Flowable.error(WowMountExceptions.AuthorizedException())

        mMountsPresenter.loadCharacter()

        (mKodeinRule.schedulersProvider.executorThread() as TestScheduler).triggerActions()

        verifySequence {
            mKodeinRule.characterInteractor.get()
            mViewState.showRequestErrorDialog(any())
            mKodeinRule.router.newRootScreen(SplashFragment::class.java.canonicalName, any())
        }
    }

    @Test
    fun onMountClick() {
        mMountsPresenter.onMountClick("iconTransitionName", MOUNT_1)

        verifySequence {
            mKodeinRule.router.navigateTo(MountFragment::class.java.canonicalName, any())
        }
    }

    @Test
    fun onAboutClick() {
        mMountsPresenter.onAboutClick()

        verifySequence {
            mKodeinRule.router.navigateTo(AboutFragment::class.java.canonicalName)
        }
    }

    @Test
    fun onFilterClick() {
        mMountsPresenter.onFilterClick()

        verifySequence {
            mKodeinRule.router.navigateTo(FilterFragment::class.java.canonicalName, any())
        }
    }

    /**
     * Normal case
     */
    @Test
    fun onLogoutClick_1() {
        every { mKodeinRule.characterInteractor.clear() } returns Completable.complete()
        every { mKodeinRule.localPreferences.clear() } returns Unit

        mMountsPresenter.onLogoutClick()

        verifySequence {
            with(mKodeinRule) {
                characterInteractor.clear()
                localPreferences.clear()
                router.newRootScreen(AuthorizationFragment::class.java.canonicalName)
            }
        }
    }

    /**
     * Exception
     */
    @Test
    fun onLogoutClick_2() {
        every { mKodeinRule.characterInteractor.clear() } returns Completable.error(Exception())

        mMountsPresenter.onLogoutClick()

        verifySequence {
            mKodeinRule.characterInteractor.clear()
            mViewState.showLogoutErrorDialog()
        }
    }

    @Test
    fun onMountsStartScrolling() {
        mMountsPresenter.onMountsStartScrolling()

        verifySequence {
            mViewState.setPagerSwipeEnable(false)
        }
    }

    @Test
    fun onMountsStopScrolling() {
        mMountsPresenter.onMountsStopScrolling()

        verifySequence {
            mViewState.setPagerSwipeEnable(true)
        }
    }

    /**
     * Normal case
     */
    @Test
    fun onRefresh_1() {
        every { mKodeinRule.characterInteractor.update() } returns Single.just(Pair(CHARACTER, MOUNT_LIST))

        mMountsPresenter.onRefresh()

        verifySequence {
            mKodeinRule.characterInteractor.update()
            mViewState.setCharacter(CHARACTER)
            mViewState.setMounts(MOUNT_LIST)
            mViewState.stopRefresh()
        }
    }

    /**
     * Inversed mount list
     */
    @Test
    fun onRefresh_2() {
        every { mKodeinRule.characterInteractor.update() } returns Single.just(Pair(CHARACTER, listOf(MOUNT_4, MOUNT_3, MOUNT_2, MOUNT_1)))

        mMountsPresenter.onRefresh()

        verifySequence {
            mKodeinRule.characterInteractor.update()
            mViewState.setCharacter(CHARACTER)
            mViewState.setMounts(MOUNT_LIST)
            mViewState.stopRefresh()
        }
    }

    /**
     * IOException
     */
    @Test
    fun onRefresh_3() {
        every { mKodeinRule.characterInteractor.update() } returns Single.error(WowMountExceptions.IOException())

        mMountsPresenter.onRefresh()

        verifySequence {
            mKodeinRule.characterInteractor.update()
            mViewState.stopRefresh()
            mViewState.showRequestErrorDialog(any())
        }
    }

    /**
     * AuthorizedException
     */
    @Test
    fun onRefresh_4() {
        every { mKodeinRule.characterInteractor.update() } returns Single.error(WowMountExceptions.AuthorizedException())

        mMountsPresenter.onRefresh()

        verifySequence {
            mKodeinRule.characterInteractor.update()
            mViewState.stopRefresh()
            mViewState.showRequestErrorDialog(any())
            mKodeinRule.router.newRootScreen(SplashFragment::class.java.canonicalName, any())
        }
    }
}