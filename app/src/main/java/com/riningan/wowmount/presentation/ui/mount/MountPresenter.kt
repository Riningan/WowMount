package com.riningan.wowmount.presentation.ui.mount

import com.arellomobile.mvp.InjectViewState
import com.riningan.frarg.annotations.Argument
import com.riningan.frarg.annotations.ArgumentedFragment
import com.riningan.frarg.processor.SplashFragmentArgs
import com.riningan.util.Logger
import com.riningan.wowmount.domain.interactor.CharacterInteractor
import com.riningan.wowmount.domain.WowMountExceptions
import com.riningan.wowmount.presentation.ui.base.BasePresenter
import com.riningan.wowmount.presentation.ui.splash.SplashFragment
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import ru.terrakok.cicerone.Router


@InjectViewState
@ArgumentedFragment(fragmentClass = MountFragment::class)
class MountPresenter constructor(kodein: Kodein) : BasePresenter<MountView>() {
    @Argument
    private lateinit var mIconTransitionName: String
    @Argument
    private lateinit var mMountId: String
    private val mRouter: Router by kodein.instance()
    private val mCharacterInteractor: CharacterInteractor by kodein.instance()


    fun setupTransitionView() {
        Logger.debug()
        viewState.setTransition(mIconTransitionName)
    }

    fun setupMount() {
        Logger.debug()
        mCharacterInteractor
                .getMountById(mMountId)
                .subscribe({
                    viewState.setMount(it)
                }, {
                    viewState.showError(it.localizedMessage)
                    if (it is WowMountExceptions.AuthorizedException) {
                        mRouter.newRootScreen(SplashFragment::class.java.canonicalName, SplashFragmentArgs(true))
                    }
                })
                .attach()
    }

    fun onBackClick() {
        Logger.debug()
        mRouter.exit()
    }
}