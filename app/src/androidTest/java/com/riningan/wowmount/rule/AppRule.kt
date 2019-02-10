package com.riningan.wowmount.rule

import android.support.test.rule.ActivityTestRule
import com.riningan.wowmount.presentation.ui.base.BaseFragment
import com.riningan.wowmount.test.TestActivity
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement


class AppRule : TestRule {
    private val mKodeinDIMockRule = KodeinDIMockRule()
    private val mActivityTestRule = ActivityTestRule(TestActivity::class.java, true, true)
    private var mRuleChain: RuleChain = RuleChain
            .outerRule(mKodeinDIMockRule)
            .around(mActivityTestRule)


    override fun apply(base: Statement?, description: Description?): Statement =
            mRuleChain.apply(base, description)


    fun launch(fragmentClass: Class<out BaseFragment>, data: Any? = null) {
        mActivityTestRule.activity.setFragment(fragmentClass, data)
    }


    fun getActivity() = mActivityTestRule.activity

    fun getMockedLocalPreferences() = mKodeinDIMockRule.localPreferences
}