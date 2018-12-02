package com.riningan.wowmount.ui.splash

import com.arellomobile.mvp.InjectViewState
import com.riningan.wowmount.data.LocalPreferences
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


    fun onStart() {
        LogUtil.addDebug()
        Observable
                .timer(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (mLocalPreferences.characterName.isEmpty()) {
                        mRouter.navigateTo(AuthorizationFragment::class.java.canonicalName)
                    } else {
                        mRouter.navigateTo(MountsFragment::class.java.canonicalName)
                    }
                }
                .attach()
    }
}