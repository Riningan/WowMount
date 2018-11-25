package com.riningan.wowmount.ui.authorization

import com.arellomobile.mvp.InjectViewState
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
    private val mMountsInteractor: CharacterInteractor by kodein.instance()


    fun onShowClick(server: String, realmName: String, characterName: String) {
        LogUtil.addDebug("server", server, "realmName", realmName, "characterName", characterName)
        when {
            server.isEmpty() -> viewState.showServerErrorDialog()
            realmName.isEmpty() -> viewState.showRealmErrorDialog()
            characterName.isEmpty() -> viewState.showCharacterErrorDialog()
            else -> {
                viewState.lockView()
                mMountsInteractor
                        .update(server, realmName, characterName)
                        .subscribe({
                            mRouter.newRootScreen(MountsFragment::class.java.canonicalName)
                        }, {
                            viewState.unlockView()
                            viewState.showRequestErrorDialog(it.message!!)
                        })
                        .attach()
            }
        }
    }
}