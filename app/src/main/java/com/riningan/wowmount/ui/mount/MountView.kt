package com.riningan.wowmount.ui.mount

import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.riningan.wowmount.data.model.Mount
import com.riningan.wowmount.ui.base.BaseView


@StateStrategyType(SingleStateStrategy::class)
interface MountView : BaseView {
    fun showMount(mount: Mount)
    fun showError()
}