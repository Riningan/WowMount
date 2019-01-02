package com.riningan.wowmount.data.db.model

import io.realm.RealmObject


open class MountEntity : RealmObject() {
    var id = ""
    var name = ""
    var itemId = 0
    var qualityId = 0
    var clientId = 0
    var icon: String? = null
    var isGround = false
    var isFlying = false
    var isAquatic = false
    var isCollected = false
}