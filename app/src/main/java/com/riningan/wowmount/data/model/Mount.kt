package com.riningan.wowmount.data.model

import io.realm.RealmObject


open class Mount : RealmObject() {
    var id = ""
    var name = ""
    var itemId = 0
    var qualityId = 0
    var icon: String? = null
    var isGround = false
    var isFlying = false
    var isAquatic = false
    var isCollected = false


    fun getIconUrl() = if (icon != null) "https://render-us.worldofwarcraft.com/icons/56/$icon.jpg" else null
}