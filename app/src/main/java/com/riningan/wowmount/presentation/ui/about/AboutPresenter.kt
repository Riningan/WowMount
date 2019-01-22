package com.riningan.wowmount.presentation.ui.about

import com.arellomobile.mvp.InjectViewState
import com.riningan.util.Logger
import com.riningan.wowmount.presentation.ui.base.BasePresenter
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import ru.terrakok.cicerone.Router


@InjectViewState
class AboutPresenter constructor(kodein: Kodein) : BasePresenter<AboutView>() {
    private val mRouter: Router by kodein.instance()


    fun onBackClick() {
        Logger.debug()
        mRouter.exit()
    }
}