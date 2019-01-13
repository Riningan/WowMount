package com.riningan.wowmount.rule

import com.riningan.wowmount.data.network.model.MountResponse
import com.riningan.wowmount.data.network.model.MountsResponse
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement


class MountsResponseRule : TestRule {
    private lateinit var mResponse: MountsResponse


    override fun apply(base: Statement, description: Description?) = object : Statement() {
        @Throws(Throwable::class)
        override fun evaluate() {
            mResponse = MountsResponse(MOUNT_LIST)
            base.evaluate()
        }
    }


    fun getResponse() = mResponse


    companion object {
        val MOUNT_1 = MountResponse(
                "Test mount 1",
                1,
                1,
                1,
                1,
                "Test mount 1 Icon",
                true,
                true,
                true,
                true)

        val MOUNT_2 = MountResponse(
                "Test mount 2",
                2,
                2,
                2,
                2,
                "Test mount 2 Icon",
                true,
                false,
                true,
                true)

        val MOUNT_3 = MountResponse(
                "Test mount 3",
                3,
                3,
                3,
                3,
                "Test mount 3 Icon",
                false,
                true,
                true,
                true)

        val MOUNT_4 = MountResponse(
                "Test mount 4",
                4,
                4,
                4,
                4,
                "Test mount 4 Icon",
                true,
                false,
                false,
                false)

        val MOUNT_LIST = listOf(MOUNT_1, MOUNT_2, MOUNT_3, MOUNT_4)
    }
}