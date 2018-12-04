package com.riningan.wowmount.ui.mount

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.riningan.frarg.FrargBinder
import com.riningan.frarg.annotations.Argument
import com.riningan.frarg.annotations.ArgumentedFragment
import com.riningan.wowmount.interactors.CharacterInteractor
import com.riningan.wowmount.ui.base.BasePresenter
import com.riningan.wowmount.utils.LogUtil
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import ru.terrakok.cicerone.Router


@InjectViewState
@ArgumentedFragment(fragmentClass = MountFragment::class)
class MountPresenter constructor(kodein: Kodein) : BasePresenter<MountView>() {
    @Argument
    private var mMountId: Int = 0
    private val mRouter: Router by kodein.instance()
    private val mCharacterInteractor: CharacterInteractor by kodein.instance()


    fun bind(arguments: Bundle) {
        LogUtil.addDebug()
        FrargBinder.bind(this, arguments)
    }

    fun onStart() {
        LogUtil.addDebug()
        mCharacterInteractor
                .getMountByItemId(mMountId)
                .subscribe({
                    viewState.showMount(it)
                }, {
                    viewState.showError()
                })
                .attach()
    }

    fun onBackClick() {
        mRouter.exit()
    }
}