package com.riningan.wowmount.data.db

import io.realm.Realm


interface DBHelper {
    fun getDBInstance(): Realm
}