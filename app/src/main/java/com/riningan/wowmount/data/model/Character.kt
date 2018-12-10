package com.riningan.wowmount.data.model

import io.realm.RealmObject


open class Character : RealmObject() {
    var name: String = ""
    var realm: String = ""
    var level: Int = 0
    var thumbnail: String = ""
    var region: String = ""


    /**
     * generated
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Character
        if (name != other.name) return false
        if (realm != other.realm) return false
        if (level != other.level) return false
        if (thumbnail != other.thumbnail) return false
        if (region != other.region) return false
        return true
    }

    /**
     * generated
     */
    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + realm.hashCode()
        result = 31 * result + level
        result = 31 * result + thumbnail.hashCode()
        result = 31 * result + region.hashCode()
        return result
    }


    fun getAvatarUrl() = "http://render-$region.worldofwarcraft.com/character/$thumbnail"


    fun getMainUrl() = "http://render-$region.worldofwarcraft.com/character/${thumbnail.replace("-avatar.jpg", "-main.jpg")}"
}