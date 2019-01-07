package com.riningan.wowmount.ui.mount

import com.arellomobile.mvp.InjectViewState
import com.riningan.frarg.annotations.Argument
import com.riningan.frarg.annotations.ArgumentedFragment
import com.riningan.util.Logger
import com.riningan.wowmount.interactors.CharacterInteractor
import com.riningan.wowmount.ui.base.BasePresenter
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
                .getMountByItemId(mMountId)
                .subscribe({
                    viewState.setMount(it)
                }, {
                    // todo
                    viewState.showError(it.localizedMessage)
                })
                .attach()
    }

    fun onBackClick() {
        Logger.debug()
        mRouter.exit()
    }
}