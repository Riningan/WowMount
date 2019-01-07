package com.riningan.wowmount.ui.mounts

import com.arellomobile.mvp.InjectViewState
import com.riningan.frarg.annotations.Argument
import com.riningan.frarg.annotations.ArgumentedFragment
import com.riningan.frarg.processor.FilterFragmentArgs
import com.riningan.frarg.processor.MountFragmentArgs
import com.riningan.frarg.processor.SplashFragmentArgs
import com.riningan.util.Logger
import com.riningan.wowmount.data.preferences.LocalPreferences
import com.riningan.wowmount.data.repository.model.Character
import com.riningan.wowmount.data.repository.model.Mount
import com.riningan.wowmount.interactors.CharacterInteractor
import com.riningan.wowmount.interactors.WowMountExceptions
import com.riningan.wowmount.ui.about.AboutFragment
import com.riningan.wowmount.ui.authorization.AuthorizationFragment
import com.riningan.wowmount.ui.base.BasePresenter
import com.riningan.wowmount.ui.filter.FilterFragment
import com.riningan.wowmount.ui.mount.MountFragment
import com.riningan.wowmount.ui.splash.SplashFragment
import ru.terrakok.cicerone.Router


@InjectViewState
@ArgumentedFragment(fragmentClass = MountsFragment::class)
class MountsPresenter constructor(private val mRouter: Router,
                                  private val mCharacterInteractor: CharacterInteractor,
                                  private val mLocalPreferences: LocalPreferences) : BasePresenter<MountsView>() {
    @Argument
    private var mShowAll: Boolean = true


    override fun clearSubscriptions() {
        super.clearSubscriptions()
        viewState.stopRefresh()
    }

    fun loadCharacter() {
        Logger.debug()
        mCharacterInteractor
                .get()
                .map { pair -> if (mShowAll) pair else Pair(pair.first, pair.second.filter { it.isCollected }) }
                .map { (character, mounts) -> Pair(character, mounts.sortedWith(compareBy(Mount::name))) }
                .subscribe({ (character, mounts) ->
                    setData(character, mounts)
                }, {
                    viewState.showRequestErrorDialog(it.localizedMessage)
                    if (it is WowMountExceptions.AuthorizedException) {
                        mRouter.newRootScreen(SplashFragment::class.java.canonicalName, SplashFragmentArgs(true))
                    }
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

    fun onFilterClick() {
        Logger.debug()
        mRouter.navigateTo(FilterFragment::class.java.canonicalName, FilterFragmentArgs(mShowAll))
    }

    fun onLogoutClick() {
        Logger.debug()
        mCharacterInteractor
                .clear()
                .subscribe({
                    mLocalPreferences.clear()
                    mRouter.newRootScreen(AuthorizationFragment::class.java.canonicalName)
                }, {
                    viewState.showLogoutErrorDialog()
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
                    viewState.showRequestErrorDialog(it.localizedMessage)
                    if (it is WowMountExceptions.AuthorizedException) {
                        mRouter.newRootScreen(SplashFragment::class.java.canonicalName, SplashFragmentArgs(true))
                    }
                }, {
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