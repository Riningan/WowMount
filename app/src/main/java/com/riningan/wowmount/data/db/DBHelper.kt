package com.riningan.wowmount.data.db

import io.realm.Realm


class DBHelper {
    fun getDBInstance(): Realm = Realm.getDefaultInstance()
}