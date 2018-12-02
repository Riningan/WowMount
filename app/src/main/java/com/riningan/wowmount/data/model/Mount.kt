package com.riningan.wowmount.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class Mount: RealmObject() {
    @PrimaryKey
    var itemId = 0
    var name = ""
    var qualityId = 0
    var icon = ""
    var isGround = false
    var isFlying = false
    var isAquatic = false
    var isCollected = false
}