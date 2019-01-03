package com.riningan.wowmount.ui.mounts

import com.arellomobile.mvp.InjectViewState
import com.riningan.frarg.processor.MountFragmentArgs
import com.riningan.util.Logger
import com.riningan.wowmount.data.preferences.LocalPreferences
import com.riningan.wowmount.data.repository.model.Character
import com.riningan.wowmount.data.repository.model.Mount
import com.riningan.wowmount.interactors.CharacterInteractor
import com.riningan.wowmount.ui.about.AboutFragment
import com.riningan.wowmount.ui.authorization.AuthorizationFragment
import com.riningan.wowmount.ui.base.BasePresenter
import com.riningan.wowmount.ui.mount.MountFragment
import ru.terrakok.cicerone.Router


@InjectViewState
class MountsPresenter constructor(private val mRouter: Router,
                                  private val mCharacterInteractor: CharacterInteractor,
                                  private val mLocalPreferences: LocalPreferences) : BasePresenter<MountsView>() {
    override fun clearSubscriptions() {
        super.clearSubscriptions()
        viewState.stopRefresh()
    }

    fun loadCharacter() {
        Logger.debug()
        mCharacterInteractor
                .get()
                .subscribe({ (character, mounts) ->
                    setData(character, mounts)
                }, {
                    viewState.showErrorDialog(it.localizedMessage)
                })
                .attach()
    }

    fun onMountClick(iconTransitionName: String, mount: Mount) {
        Logger.debug("mount", mount.name)
        mRouter.navigateTo(MountFragment::class.java.canonicalName, MountFragmentArgs(iconTransitionName, mount.id))
    }

    fun onAboutClick() {
        Logger.debug()
        mRouter.navigateTo(AboutFragment::class.java.canonicalName)
    }

    fun onLogoutClick() {
        Logger.debug()
        mCharacterInteractor
                .clear()
                .subscribe({
                    mLocalPreferences.clear()
                    mRouter.newRootScreen(AuthorizationFragment::class.java.canonicalName)
                }, {
                    viewState.showErrorDialog(it.localizedMessage)
                })
                .attach()
    }

    fun onMountsStartScrolling() {
//         Logger.debug() much calls
        viewState.setPagerSwipeEnable(false)
    }

    fun onMountsStopScrolling() {
        Logger.debug()
        viewState.setPagerSwipeEnable(true)
    }

    fun onRefresh() {
        Logger.debug()
        mCharacterInteractor
                .update()
                .subscribe({ (character, mounts) ->
                    setData(character, mounts)
                }, {
                    viewState.stopRefresh()
                    viewState.showErrorDialog(it.localizedMessage)
                }, {
                    Logger.forThis(this@MountsPresenter).debug()
                    viewState.stopRefresh()
                })
                .attach()
    }


    private fun setData(character: Character, mounts: List<Mount>) {
        Logger.debug()
        viewState.setCharacter(character)
        viewState.setMounts(mounts)
    }


    companion object {
        const val TAG = "MountsPresenter"
    }
}