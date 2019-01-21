package com.riningan.wowmount.ui

import com.arellomobile.mvp.MvpPresenter
import com.riningan.wowmount.ui.splash.SplashFragment
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import ru.terrakok.cicerone.Router


class MainPresenter(kodein: Kodein) : MvpPresenter<MainView>() {
    private val mRouter: Router by kodein.instance()


    fun startNavigation() {
        mRouter.newRootScreen(SplashFragment::class.java.canonicalName)
    }
}