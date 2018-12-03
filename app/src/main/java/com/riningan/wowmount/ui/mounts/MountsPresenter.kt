package com.riningan.wowmount.ui.mounts

import com.arellomobile.mvp.InjectViewState
import com.riningan.wowmount.data.model.Mount
import com.riningan.wowmount.interactors.CharacterInteractor
import com.riningan.wowmount.ui.base.BasePresenter
import com.riningan.wowmount.utils.LogUtil
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import ru.terrakok.cicerone.Router


@InjectViewState
class MountsPresenter constructor(kodein: Kodein) : BasePresenter<MountsView>() {
    private val mRouter: Router by kodein.instance()
    private val mCharacterInteractor: CharacterInteractor by kodein.instance()


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
        mRouter.navigateTo(MountsFragment::class.java.canonicalName)
    }


    companion object {
        const val TAG = "MountsPresenter"
    }
}