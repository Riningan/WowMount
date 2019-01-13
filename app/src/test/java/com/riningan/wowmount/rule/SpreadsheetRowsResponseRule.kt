package com.riningan.wowmount.rule

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


    companion object {
        val ROW_1 = SpreadsheetCSVResponse(
                MountsResponseRule.MOUNT_1.spellId,
                MountsResponseRule.MOUNT_1.creatureId,
                MountsResponseRule.MOUNT_1.itemId,
                101,
                MountsResponseRule.MOUNT_1.name
        )

        val ROW_2 = SpreadsheetCSVResponse(
                MountsResponseRule.MOUNT_2.spellId,
                MountsResponseRule.MOUNT_2.creatureId,
                MountsResponseRule.MOUNT_2.itemId,
                102,
                MountsResponseRule.MOUNT_2.name
        )

        val ROW_3 = SpreadsheetCSVResponse(
                MountsResponseRule.MOUNT_3.spellId,
                MountsResponseRule.MOUNT_3.creatureId,
                MountsResponseRule.MOUNT_3.itemId,
                103,
                MountsResponseRule.MOUNT_3.name
        )

        val ROW_LIST = listOf(ROW_1, ROW_2, ROW_3)
    }
}