package com.riningan.wowmount

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement


class LogRule : TestRule {
    override fun apply(base: Statement, description: Description) = object : Statement() {
        override fun evaluate() {
            val className = description.testClass.canonicalName
            System.out.print("Test name: " + if (className != null) {
                "$className.${description.displayName.replace("($className)", "")}"
            } else {
                description.displayName
            })
            try {
                base.evaluate()
                System.out.println(" - Success")
            } catch (e: Throwable) {
                System.out.println(" - Fail")
                throw e
            }
        }
    }
}