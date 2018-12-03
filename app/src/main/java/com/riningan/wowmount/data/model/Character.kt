package com.riningan.wowmount.data.model

import io.realm.RealmObject


open class Character: RealmObject() {
    var name: String = ""
    var realm: String = ""
    var level: Int = 0
    var thumbnail: String = ""
}