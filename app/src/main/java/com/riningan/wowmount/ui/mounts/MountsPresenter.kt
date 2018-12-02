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


    fun onStart() {
        LogUtil.addDebug()
        mCharacterInteractor
                .get()
                .subscribe({
                    LogUtil.addDebug(this@MountsPresenter)
                }, {
                    LogUtil.addError(this@MountsPresenter, it)
                }, {
                    LogUtil.addDebug(this@MountsPresenter)
                })
                .attach()
    }

    fun onStop() {
        LogUtil.addDebug()
        clearSubscriptions()
    }

    fun onMountClick(mount: Mount) {
        LogUtil.addDebug("mount", mount.name)
        mRouter.navigateTo(MountsFragment::class.java.canonicalName)
    }


    companion object {
        const val TAG = "MountsPresenter"
    }
}