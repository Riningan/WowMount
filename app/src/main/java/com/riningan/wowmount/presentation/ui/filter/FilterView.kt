package com.riningan.wowmount.presentation.ui.filter

import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.riningan.wowmount.presentation.ui.base.BaseView


@StateStrategyType(SingleStateStrategy::class)
interface FilterView : BaseView {
    fun setFilter(showAll: Boolean)
    fun showProgress()
    fun showError(message: String)
    fun showButton(count: Int)
}