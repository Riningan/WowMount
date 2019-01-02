package com.riningan.wowmount.ui.about

import com.riningan.wowmount.ui.base.BasePresenter
import com.riningan.wowmount.utils.LogUtil
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import ru.terrakok.cicerone.Router


class AboutPresenter constructor(kodein: Kodein) : BasePresenter<AboutView>() {
    private val mRouter: Router by kodein.instance()


    fun onBackClick() {
        LogUtil.addDebug()
        mRouter.exit()
    }
}