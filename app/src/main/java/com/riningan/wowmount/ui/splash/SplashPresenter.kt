package com.riningan.wowmount.ui.splash

import com.arellomobile.mvp.InjectViewState
import com.riningan.frarg.annotations.Argument
import com.riningan.frarg.annotations.ArgumentedFragment
import com.riningan.frarg.processor.MountsFragmentArgs
import com.riningan.util.Logger
import com.riningan.wowmount.data.preferences.LocalPreferences
import com.riningan.wowmount.interactor.CharacterInteractor
import com.riningan.wowmount.interactor.WowMountExceptions
import com.riningan.wowmount.ui.authorization.AuthorizationFragment
import com.riningan.wowmount.ui.base.BasePresenter
import com.riningan.wowmount.ui.mounts.MountsFragment
import com.riningan.wowmount.utils.SchedulersProvider
import io.reactivex.Observable
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import ru.terrakok.cicerone.Router
import java.util.concurrent.TimeUnit


@InjectViewState
@ArgumentedFragment(fragmentClass = SplashFragment::class)
class SplashPresenter constructor(kodein: Kodein) : BasePresenter<SplashView>() {
    private val mRouter: Router by kodein.instance()
    private val mLocalPreferences: LocalPreferences by kodein.instance()
    private val mCharacterInteractor: CharacterInteractor by kodein.instance()
    private val mSchedulersProvider: SchedulersProvider by kodein.instance()
    @Argument(optional = true)
    private var mClear: Boolean = false


    fun onStart() {
        Logger.debug()
        when {
            mClear -> logout()
            mLocalPreferences.isActivated -> mCharacterInteractor
                    .update()
                    .subscribe({
                        mRouter.newRootScreen(MountsFragment::class.java.canonicalName, MountsFragmentArgs(mLocalPreferences.showAll))
                    }, {
                        viewState.showErrorDialog(it.localizedMessage)
                        if (it is WowMountExceptions.AuthorizedException) {
                            mClear = true
                            logout()
                        }
                    })
                    .attach()
            else -> Observable
                    .timer(3, TimeUnit.SECONDS)
                    .subscribeOn(mSchedulersProvider.executorThread())
                    .observeOn(mSchedulersProvider.postExecutionThread())
                    .subscribe { mRouter.navigateTo(AuthorizationFragment::class.java.canonicalName) }
                    .attach()
        }
    }


    private fun logout() {
        Logger.debug()
        mCharacterInteractor
                .clear()
                .subscribe({
                    mLocalPreferences.clear()
                    mRouter.newRootScreen(AuthorizationFragment::class.java.canonicalName)
                }, {
                    logout()
                })
                .attach()
    }
}