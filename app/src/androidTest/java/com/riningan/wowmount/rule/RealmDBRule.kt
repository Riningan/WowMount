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
            Realm.init(mAppContext)
            RealmConfiguration.Builder()
                    .inMemory()
                    .name("test-realm")
                    .build()
            Realm.getDefaultInstance().apply {
                beginTransaction()
//                copyToRealm(java.getCHARACTER_ENTITY)
//                java.getMOUNT_ENTITY_LIST.forEach { copyToRealm(it) }
                commitTransaction()
                close()
            }
            base.evaluate()
        }
    }


    fun getRealm(): Realm = Realm.getDefaultInstance()
}