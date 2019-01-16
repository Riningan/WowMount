package com.riningan.wowmount.rule

import com.riningan.wowmount.MOUNT_RESPONSE_LIST
import com.riningan.wowmount.data.network.model.MountsResponse
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement


class MountsResponseRule : TestRule {
    private lateinit var mResponse: MountsResponse


    override fun apply(base: Statement, description: Description?) = object : Statement() {
        @Throws(Throwable::class)
        override fun evaluate() {
            mResponse = MountsResponse(MOUNT_RESPONSE_LIST)
            base.evaluate()
        }
    }


    fun getResponse() = mResponse
}