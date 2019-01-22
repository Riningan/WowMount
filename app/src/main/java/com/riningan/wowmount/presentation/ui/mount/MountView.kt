package com.riningan.wowmount.presentation.ui.mount

import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.riningan.wowmount.data.repository.model.Mount
import com.riningan.wowmount.presentation.ui.base.BaseView


@StateStrategyType(SingleStateStrategy::class)
interface MountView : BaseView {
    fun setTransition(transitionName: String)
    fun setMount(mount: Mount)
    fun showError(message: String)
}