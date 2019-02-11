package com.riningan.wowmount.data.db

import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration


class DBHelperImpl(context: Context) : DBHelper {
    init {
        Realm.init(context)
        val config = RealmConfiguration.Builder()
                .name("wowmount.realm")
                .schemaVersion(1)
                .build()
        Realm.setDefaultConfiguration(config)
    }


    override fun getDBInstance(): Realm = Realm.getDefaultInstance()
}