package com.riningan.wowmount.ui.authorization

import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.riningan.wowmount.ui.base.BaseView

@StateStrategyType(SkipStrategy::class)
interface AuthorizationView : BaseView {
    fun showServerErrorDialog()
    fun showRealmErrorDialog()
    fun showCharacterErrorDialog()
    fun showRequestErrorDialog(message: String)

    @StateStrategyType(SingleStateStrategy::class)
    fun lockView()

    @StateStrategyType(SingleStateStrategy::class)
    fun unlockView()
}