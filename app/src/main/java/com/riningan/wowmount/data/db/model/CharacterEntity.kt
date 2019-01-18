package com.riningan.wowmount.data.db.model

import io.realm.RealmObject


open class CharacterEntity : RealmObject() {
    var name: String = ""
    var realm: String = ""
    var level: Int = 0
    var thumbnail: String = ""
    var region: String = ""
}