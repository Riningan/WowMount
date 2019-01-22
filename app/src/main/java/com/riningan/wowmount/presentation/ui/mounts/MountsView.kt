package com.riningan.wowmount.presentation.ui.mounts

import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.riningan.wowmount.data.repository.model.Character
import com.riningan.wowmount.data.repository.model.Mount
import com.riningan.wowmount.presentation.ui.base.BaseView


@StateStrategyType(SingleStateStrategy::class)
interface MountsView : BaseView {
    fun setCharacter(character: Character)
    fun setMounts(mounts: List<Mount>)

    @StateStrategyType(SkipStrategy::class)
    fun showRequestErrorDialog(message: String)

    @StateStrategyType(SkipStrategy::class)
    fun showLogoutErrorDialog()

    @StateStrategyType(SkipStrategy::class)
    fun setPagerSwipeEnable(isEnabled: Boolean)

    @StateStrategyType(SkipStrategy::class)
    fun stopRefresh()
}