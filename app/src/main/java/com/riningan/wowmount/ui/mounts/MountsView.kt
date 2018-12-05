package com.riningan.wowmount.ui.mounts

import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.riningan.wowmount.data.model.Character
import com.riningan.wowmount.data.model.Mount
import com.riningan.wowmount.ui.base.BaseView


@StateStrategyType(SingleStateStrategy::class)
interface MountsView : BaseView {
    fun setCharacter(character: Character)
    fun setMounts(mounts: List<Mount>)

    @StateStrategyType(SkipStrategy::class)
    fun showErrorDialog(message: String)

    @StateStrategyType(SkipStrategy::class)
    fun setPagerSwipeEnable(isEnabled: Boolean)
}