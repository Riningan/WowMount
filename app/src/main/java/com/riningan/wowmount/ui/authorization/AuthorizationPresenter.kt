package com.riningan.wowmount.ui.authorization

import com.arellomobile.mvp.InjectViewState
import com.riningan.wowmount.data.preferences.LocalPreferences
import com.riningan.wowmount.interactors.CharacterInteractor
import com.riningan.wowmount.ui.base.BasePresenter
import com.riningan.wowmount.ui.mounts.MountsFragment
import com.riningan.wowmount.utils.LogUtil
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import ru.terrakok.cicerone.Router


@InjectViewState
class AuthorizationPresenter constructor(kodein: Kodein) : BasePresenter<AuthorizationView>() {
    private val mRouter: Router by kodein.instance()
    private val mLocalPreferences: LocalPreferences by kodein.instance()
    private val mCharacterInteractor: CharacterInteractor by kodein.instance()


    fun onShowClick(server: String, realmName: String, characterName: String) {
        LogUtil.addDebug("server", server, "realmName", realmName, "characterName", characterName)
        when {
            server.isEmpty() -> viewState.showServerErrorDialog()
            realmName.isEmpty() -> viewState.showRealmErrorDialog()
            characterName.isEmpty() -> viewState.showCharacterErrorDialog()
            else -> {
                viewState.lockView()
                mLocalPreferences.apply {
                    this.server = server
                    this.realmName = realmName
                    this.characterName = characterName
                }
                mCharacterInteractor
                        .get()
                        .subscribe({
                            mLocalPreferences.isActivated = true
                            mRouter.newRootScreen(MountsFragment::class.java.canonicalName)
                        }, {
                            mLocalPreferences.clear()
                            viewState.unlockView()
                            viewState.showRequestErrorDialog(it.message!!)
                        })
                        .attach()
            }
        }
    }
}