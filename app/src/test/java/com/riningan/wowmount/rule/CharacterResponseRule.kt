package com.riningan.wowmount.rule

import com.riningan.wowmount.CHARACTER_RESPONSE
import com.riningan.wowmount.data.network.model.CharacterResponse
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement


class CharacterResponseRule : TestRule {
    private lateinit var mResponse: CharacterResponse


    override fun apply(base: Statement, description: Description?) = object : Statement() {
        @Throws(Throwable::class)
        override fun evaluate() {
            mResponse = CHARACTER_RESPONSE
            base.evaluate()
        }
    }


    fun getResponse() = mResponse
}