package com.riningan.wowmount.rule

import com.riningan.util.Logger
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement


class LoggerDisableRule : TestRule {
    override fun apply(base: Statement, description: Description?) = object : Statement() {
        override fun evaluate() {
            Logger.Config.setEnabled(false)
            base.evaluate()
        }
    }
}