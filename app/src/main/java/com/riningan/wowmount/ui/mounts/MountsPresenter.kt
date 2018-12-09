package com.riningan.wowmount.ui.mounts

import com.arellomobile.mvp.InjectViewState
import com.riningan.frarg.processor.MountFragmentArgs
import com.riningan.wowmount.data.model.Character
import com.riningan.wowmount.data.model.Mount
import com.riningan.wowmount.interactors.CharacterInteractor
import com.riningan.wowmount.ui.base.BasePresenter
import com.riningan.wowmount.ui.mount.MountFragment
import com.riningan.wowmount.utils.LogUtil
import ru.terrakok.cicerone.Router


@InjectViewState
class MountsPresenter constructor(private val mRouter: Router,
                                  private val mCharacterInteractor: CharacterInteractor) : BasePresenter<MountsView>() {
    override fun clearSubscriptions() {
        super.clearSubscriptions()
        viewState.stopRefresh()
    }

    fun loadCharacter() {
        LogUtil.addDebug()
        mCharacterInteractor
                .get()
                .subscribe({ (character, mounts) ->
                    setData(character, mounts)
                }, {
                    viewState.showErrorDialog(it.localizedMessage)
                }, {
                    LogUtil.addDebug(this@MountsPresenter)
                })
                .attach()
    }

    fun onMountClick(iconTransitionName: String, mount: Mount) {
        LogUtil.addDebug("mount", mount.name)
        mRouter.navigateTo(MountFragment::class.java.canonicalName, MountFragmentArgs(iconTransitionName, mount.id))
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

    fun onRefresh() {
        LogUtil.addDebug()
        mCharacterInteractor
                .update()
                .subscribe({ (character, mounts) ->
                    setData(character, mounts)
                }, {
                    viewState.stopRefresh()
                    viewState.showErrorDialog(it.localizedMessage)
                }, {
                    LogUtil.addDebug(this@MountsPresenter)
                    viewState.stopRefresh()
                })
                .attach()
    }


    private fun setData(character: Character, mounts: List<Mount>) {
        LogUtil.addDebug()
        viewState.setCharacter(character)
        viewState.setMounts(mounts)
    }


    companion object {
        const val TAG = "MountsPresenter"
    }
}