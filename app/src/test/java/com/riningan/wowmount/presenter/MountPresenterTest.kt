package com.riningan.wowmount.presenter

import com.riningan.wowmount.MOUNT_1
import com.riningan.wowmount.domain.WowMountExceptions
import com.riningan.wowmount.rule.KodeinMockRule
import com.riningan.wowmount.rule.LogRule
import com.riningan.wowmount.rule.LoggerDisableRule
import com.riningan.wowmount.rule.WowMountExceptionsMockRule
import com.riningan.wowmount.setPrivateField
import com.riningan.wowmount.presentation.ui.mount.MountPresenter
import com.riningan.wowmount.presentation.ui.mount.MountView
import com.riningan.wowmount.presentation.ui.splash.SplashFragment
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class MountPresenterTest {
    @get: Rule
    val mLogRule = LogRule()
    @get: Rule
    val mLoggerRule = LoggerDisableRule()
    @get: Rule
    val mKodeinRule = KodeinMockRule()
    @get: Rule
    val mWowMountExceptionsRule = WowMountExceptionsMockRule()

    private val mViewState: MountView = mockk(relaxed = true)
    private lateinit var mMountPresenter: MountPresenter


    @Before
    fun setup() {
        mMountPresenter = spyk(MountPresenter(mKodeinRule.kodein))
        every { mMountPresenter.viewState } returns mViewState
    }


    @Test
    fun setupTransitionView() {
        setPrivateField(MountPresenter::class, "mIconTransitionName", mMountPresenter, "mocked")

        mMountPresenter.setupTransitionView()

        verifySequence {
            mViewState.setTransition("mocked")
        }
    }

    /**
     * Normal case
     */
    @Test
    fun setupMount_1() {
        setPrivateField(MountPresenter::class, "mMountId", mMountPresenter, MOUNT_1.id)
        every { mKodeinRule.characterInteractor.getMountById(MOUNT_1.id) } returns Flowable.just(MOUNT_1)

        mMountPresenter.setupMount()

        verifySequence {
            mKodeinRule.characterInteractor.getMountById(MOUNT_1.id)
            mViewState.setMount(MOUNT_1)
        }
    }

    /**
     * IOException
     */
    @Test
    fun setupMount_2() {
        setPrivateField(MountPresenter::class, "mMountId", mMountPresenter, MOUNT_1.id)
        every { mKodeinRule.characterInteractor.getMountById(MOUNT_1.id) } returns Flowable.error(WowMountExceptions.IOException())

        mMountPresenter.setupMount()

        verifySequence {
            mKodeinRule.characterInteractor.getMountById(MOUNT_1.id)
            mViewState.showError(any())
        }
    }

    /**
     * AuthorizedException
     */
    @Test
    fun setupMount_3() {
        setPrivateField(MountPresenter::class, "mMountId", mMountPresenter, MOUNT_1.id)
        every { mKodeinRule.characterInteractor.getMountById(MOUNT_1.id) } returns Flowable.error(WowMountExceptions.AuthorizedException())

        mMountPresenter.setupMount()

        verifySequence {
            mKodeinRule.characterInteractor.getMountById(MOUNT_1.id)
            mViewState.showError(any())
            mKodeinRule.router.newRootScreen(SplashFragment::class.java.canonicalName, any())
        }
    }

    @Test
    fun onBackClick() {
        mMountPresenter.onBackClick()

        verifySequence {
            mKodeinRule.router.exit()
        }
    }
}