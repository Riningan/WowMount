package com.riningan.wowmount.presentation.ui.authorization

import com.arellomobile.mvp.InjectViewState
import com.riningan.frarg.processor.MountsFragmentArgs
import com.riningan.util.Logger
import com.riningan.wowmount.data.preferences.LocalPreferences
import com.riningan.wowmount.domain.interactor.CharacterInteractor
import com.riningan.wowmount.presentation.ui.base.BasePresenter
import com.riningan.wowmount.presentation.ui.mounts.MountsFragment
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import ru.terrakok.cicerone.Router


@InjectViewState
class AuthorizationPresenter constructor(kodein: Kodein) : BasePresenter<AuthorizationView>() {
    private val mRouter: Router by kodein.instance()
    private val mLocalPreferences: LocalPreferences by kodein.instance()
    private val mCharacterInteractor: CharacterInteractor by kodein.instance()


    fun onShowClick(server: String, realmName: String, characterName: String) {
        Logger.debug("server", server, "realmName", realmName, "characterName", characterName)
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
                        .update()
                        .subscribe({
                            mLocalPreferences.isActivated = true
                            mRouter.newRootScreen(MountsFragment::class.java.canonicalName, MountsFragmentArgs(mLocalPreferences.showAll))
                        }, {
                            mLocalPreferences.clear()
                            viewState.unlockView()
                            viewState.showRequestErrorDialog(it.localizedMessage)
                        })
                        .attach()
            }
        }
    }
}