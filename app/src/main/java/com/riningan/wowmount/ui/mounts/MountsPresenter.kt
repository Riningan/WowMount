package com.riningan.wowmount.ui.mounts

import com.arellomobile.mvp.InjectViewState
import com.riningan.wowmount.ui.base.BasePresenter
import com.riningan.wowmount.utils.LogUtil
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import ru.terrakok.cicerone.Router

@InjectViewState
class MountsPresenter constructor(kodein: Kodein): BasePresenter<MountsView>() {
    private val mRouter: Router by kodein.instance()

    fun onStart() {
        LogUtil.addDebug()
    }

    fun onStop() {
        LogUtil.addDebug()
    }
}