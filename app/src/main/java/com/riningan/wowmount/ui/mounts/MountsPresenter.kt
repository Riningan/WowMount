package com.riningan.wowmount.ui.mounts

import com.arellomobile.mvp.InjectViewState
import com.riningan.wowmount.data.model.Mount
import com.riningan.wowmount.interactors.CharacterInteractor
import com.riningan.wowmount.ui.base.BasePresenter
import com.riningan.wowmount.ui.mount.MountFragment
import com.riningan.wowmount.utils.LogUtil
import ru.terrakok.cicerone.Router


@InjectViewState
class MountsPresenter constructor(private val mRouter: Router,
                                  private val mCharacterInteractor: CharacterInteractor) : BasePresenter<MountsView>() {
    fun loadCharacter() {
        LogUtil.addDebug()
        mCharacterInteractor
                .get()
                .subscribe({(character, mounts) ->
                    LogUtil.addDebug(this@MountsPresenter)
                    viewState.setCharacter(character)
                    viewState.setMounts(mounts)
                }, {
                    LogUtil.addError(this@MountsPresenter, it)
                    viewState.showErrorDialog(it.localizedMessage)
                }, {
                    LogUtil.addDebug(this@MountsPresenter)
                })
                .attach()
    }

    fun onMountClick(mount: Mount) {
        LogUtil.addDebug("mount", mount.name)
        mRouter.navigateTo(MountFragment::class.java.canonicalName, mount.id)
    }

    fun onLogoutClick() {
        LogUtil.addDebug()
    }

    fun onMountsStartScrolling() {
        viewState.setPagerSwipeEnable(false)
    }

    fun onMountsStopScrolling() {
        LogUtil.addDebug()
        viewState.setPagerSwipeEnable(true)
    }


    companion object {
        const val TAG = "MountsPresenter"
    }
}