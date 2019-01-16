package com.riningan.wowmount.rule

import com.riningan.wowmount.ROW_LIST
import com.riningan.wowmount.data.network.model.SpreadsheetCSVResponse
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement


class SpreadsheetRowsResponseRule : TestRule {
    private lateinit var mResponse: List<SpreadsheetCSVResponse>


    override fun apply(base: Statement, description: Description?) = object : Statement() {
        @Throws(Throwable::class)
        override fun evaluate() {
            mResponse = ROW_LIST
            base.evaluate()
        }
    }


    fun getResponse() = mResponse
}