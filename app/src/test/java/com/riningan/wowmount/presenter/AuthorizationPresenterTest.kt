package com.riningan.wowmount.presenter

import com.riningan.wowmount.*
import com.riningan.wowmount.domain.WowMountExceptions
import com.riningan.wowmount.rule.KodeinMockRule
import com.riningan.wowmount.rule.LogRule
import com.riningan.wowmount.rule.LoggerDisableRule
import com.riningan.wowmount.rule.WowMountExceptionsMockRule
import com.riningan.wowmount.presentation.ui.authorization.AuthorizationPresenter
import com.riningan.wowmount.presentation.ui.authorization.AuthorizationView
import com.riningan.wowmount.presentation.ui.mounts.MountsFragment
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class AuthorizationPresenterTest {
    @get: Rule
    val mLogRule = LogRule()
    @get: Rule
    val mLoggerRule = LoggerDisableRule()
    @get: Rule
    val mKodeinRule = KodeinMockRule()
    @get: Rule
    val mWowMountExceptionsRule = WowMountExceptionsMockRule()

    private val mViewState: AuthorizationView = mockk(relaxed = true)
    private lateinit var mAuthorizationPresenter: AuthorizationPresenter


    @Before
    fun setup() {
        mAuthorizationPresenter = spyk(AuthorizationPresenter(mKodeinRule.kodein))
        every { mAuthorizationPresenter.viewState } returns mViewState
    }


    /**
     * No server
     */
    @Test
    fun onShowClick_1() {
        mAuthorizationPresenter.onShowClick("", "", "")

        verifySequence {
            mViewState.showServerErrorDialog()
        }
    }

    /**
     * No realm
     */
    @Test
    fun onShowClick_2() {
        mAuthorizationPresenter.onShowClick(REGION, "", "")

        verifySequence {
            mViewState.showRealmErrorDialog()
        }
    }

    /**
     * No character name
     */
    @Test
    fun onShowClick_3() {
        mAuthorizationPresenter.onShowClick(REGION, REALM, "")

        verifySequence {
            mViewState.showCharacterErrorDialog()
        }
    }

    /**
     * Normal case
     */
    @Test
    fun onShowClick_4() {
        every { mKodeinRule.localPreferences.server = any() } returns Unit
        every { mKodeinRule.localPreferences.realmName = any() } returns Unit
        every { mKodeinRule.localPreferences.characterName = any() } returns Unit
        every { mKodeinRule.characterInteractor.update() } returns Single.just(Pair(CHARACTER, MOUNT_LIST))
        every { mKodeinRule.localPreferences.isActivated = any() } returns Unit
        every { mKodeinRule.localPreferences.showAll } returns true

        mAuthorizationPresenter.onShowClick(REGION, REALM, NAME)

        verifySequence {
            mViewState.lockView()
            with(mKodeinRule) {
                localPreferences.server = REGION
                localPreferences.realmName = REALM
                localPreferences.characterName = NAME
                characterInteractor.update()
                localPreferences.isActivated = true
                localPreferences.showAll
                router.newRootScreen(MountsFragment::class.java.canonicalName, any())
            }
        }
    }

    /**
     * IOException
     */
    @Test
    fun onShowClick_5() {
        every { mKodeinRule.localPreferences.server = any() } returns Unit
        every { mKodeinRule.localPreferences.realmName = any() } returns Unit
        every { mKodeinRule.localPreferences.characterName = any() } returns Unit
        every { mKodeinRule.characterInteractor.update() } returns Single.error(WowMountExceptions.IOException())
        every { mKodeinRule.localPreferences.clear() } returns Unit

        mAuthorizationPresenter.onShowClick(REGION, REALM, NAME)

        verifySequence {
            mViewState.lockView()
            with(mKodeinRule) {
                localPreferences.server = REGION
                localPreferences.realmName = REALM
                localPreferences.characterName = NAME
                characterInteractor.update()
                localPreferences.clear()
            }
            mViewState.unlockView()
            mViewState.showRequestErrorDialog(any())
        }
    }
}