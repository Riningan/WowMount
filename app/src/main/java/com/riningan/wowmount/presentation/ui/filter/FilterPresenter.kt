package com.riningan.wowmount.presentation.ui.filter

import com.arellomobile.mvp.InjectViewState
import com.riningan.frarg.annotations.Argument
import com.riningan.frarg.annotations.ArgumentedFragment
import com.riningan.frarg.processor.MountsFragmentArgs
import com.riningan.frarg.processor.SplashFragmentArgs
import com.riningan.util.Logger
import com.riningan.wowmount.data.preferences.LocalPreferences
import com.riningan.wowmount.domain.WowMountExceptions
import com.riningan.wowmount.domain.interactor.CharacterInteractor
import com.riningan.wowmount.presentation.ui.base.BasePresenter
import com.riningan.wowmount.presentation.ui.mounts.MountsFragment
import com.riningan.wowmount.presentation.ui.splash.SplashFragment
import io.reactivex.subjects.PublishSubject
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import ru.terrakok.cicerone.Router
import java.util.concurrent.TimeUnit


@InjectViewState
@ArgumentedFragment(fragmentClass = FilterFragment::class)
class FilterPresenter constructor(kodein: Kodein) : BasePresenter<FilterView>() {
    private val mRouter: Router by kodein.instance()
    private val mLocalPreferences: LocalPreferences by kodein.instance()
    private val mCharacterInteractor: CharacterInteractor by kodein.instance()
    @Argument
    private var mShowAll: Boolean = true
    private var mFilterSubject: PublishSubject<Boolean> = PublishSubject.create()


    fun onViewCreated() {
        Logger.debug()
        viewState.setFilter(mShowAll)
    }

    fun onStart() {
        Logger.debug()
        mFilterSubject
                .doOnNext { viewState.showProgress() }
                .debounce(400, TimeUnit.MILLISECONDS)
                .flatMap { mCharacterInteractor.update().toObservable() }
                .map { it.second }
                .map { mounts -> if (mShowAll) mounts else mounts.filter { it.isCollected } }
                .subscribe({
                    viewState.showButton(it.size)
                }, {
                    viewState.showError(it.localizedMessage)
                    if (it is WowMountExceptions.AuthorizedException) {
                        mRouter.newRootScreen(SplashFragment::class.java.canonicalName, SplashFragmentArgs(true))
                    }
                })
                .attach()
        mFilterSubject.onNext(mShowAll)
    }

    fun onBackClick() {
        Logger.debug()
        mRouter.exit()
    }

    fun onShowAllChecked(isChecked: Boolean) {
        Logger.debug("isChecked", isChecked)
        mShowAll = isChecked
        mFilterSubject.onNext(isChecked)
    }

    fun onShowClick() {
        Logger.debug()
        mLocalPreferences.showAll = mShowAll
        mRouter.newRootScreen(MountsFragment::class.java.canonicalName, MountsFragmentArgs(mShowAll))
    }
}