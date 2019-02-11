package com.riningan.wowmount.rule

import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement


class RealmDBRule(private val mAppContext: Context) : TestRule {
    override fun apply(base: Statement, description: Description?) = object : Statement() {
        @Throws(Throwable::class)
        override fun evaluate() {

        }
    }


    fun getRealm(): Realm = Realm.getDefaultInstance()
}