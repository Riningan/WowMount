package com.riningan.wowmount.rule

import com.riningan.wowmount.interactor.WowMountExceptions
import io.mockk.every
import io.mockk.mockkObject
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import kotlin.reflect.full.functions
import kotlin.reflect.jvm.isAccessible


class WowMountExceptionsRule : TestRule {
    override fun apply(base: Statement, description: Description?) = object : Statement() {
        override fun evaluate() {
            mockkObject(WowMountExceptions)
            every {
                WowMountExceptions.Companion::class.functions.find {
                    it.name == "getApplicationMessage"
                }!!.apply {
                    isAccessible = true
                }.call(WowMountExceptions, any<WowMountExceptions>())
            } returns "mocked"

            base.evaluate()
        }
    }
}