package com.riningan.wowmount.rule

import com.riningan.wowmount.data.network.model.CharacterMountsResponse
import com.riningan.wowmount.data.network.model.CharacterResponse
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement


class CharacterResponseRule : TestRule {
    private lateinit var mResponse: CharacterResponse


    override fun apply(base: Statement, description: Description?) = object : Statement() {
        @Throws(Throwable::class)
        override fun evaluate() {
            mResponse = CharacterResponse(
                    0L,
                    NAME,
                    REALM,
                    "battlegroup",
                    0,
                    0,
                    0,
                    LEVEL,
                    0,
                    THUMBNAIL,
                    "calsClass",
                    0,
                    CharacterMountsResponse(COLLECTED_MOUNT_LIST.size, MountsResponseRule.MOUNT_LIST.size - COLLECTED_MOUNT_LIST.size, COLLECTED_MOUNT_LIST),
                    0)
            base.evaluate()
        }
    }


    fun getResponse() = mResponse


    companion object {
        const val NAME = "name"
        const val REALM = "realm"
        const val LEVEL = 1
        const val THUMBNAIL = "thumbnail"
        val COLLECTED_MOUNT_LIST = arrayListOf(MountsResponseRule.MOUNT_1, MountsResponseRule.MOUNT_2)
    }
}