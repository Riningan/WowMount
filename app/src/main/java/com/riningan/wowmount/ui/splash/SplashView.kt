package com.riningan.wowmount.ui.splash

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.riningan.wowmount.ui.base.BaseView


@StateStrategyType(SkipStrategy::class)
interface SplashView : BaseView {
    fun showErrorDialog(message: String)
}