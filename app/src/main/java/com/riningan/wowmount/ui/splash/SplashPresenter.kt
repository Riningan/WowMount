package com.riningan.wowmount.ui.splash

import com.arellomobile.mvp.InjectViewState
import com.riningan.wowmount.data.LocalPreferences
import com.riningan.wowmount.interactors.CharacterInteractor
import com.riningan.wowmount.ui.authorization.AuthorizationFragment
import com.riningan.wowmount.ui.base.BasePresenter
import com.riningan.wowmount.ui.mounts.MountsFragment
import com.riningan.wowmount.utils.LogUtil
import java.util.concurrent.TimeUnit
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import ru.terrakok.cicerone.Router


@InjectViewState
class SplashPresenter constructor(kodein: Kodein) : BasePresenter<SplashView>() {
    private val mRouter: Router by kodein.instance()
    private val mLocalPreferences: LocalPreferences by kodein.instance()
    private val mCharacterInteractor: CharacterInteractor by kodein.instance()


    fun onStart() {
        LogUtil.addDebug()
        if (mLocalPreferences.isActivated) {
            mCharacterInteractor
                    .get()
                    .subscribe({
                    }, {
                        viewState.showErrorDialog(it.localizedMessage)
                    }, {
                        mRouter.newRootScreen(MountsFragment::class.java.canonicalName)
                    })
        } else {
            Observable
                    .timer(3, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        mRouter.navigateTo(AuthorizationFragment::class.java.canonicalName)
                    }
        }.attach()
    }
}