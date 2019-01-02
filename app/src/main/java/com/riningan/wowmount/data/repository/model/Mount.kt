package com.riningan.wowmount.data.repository.model


data class Mount(
        var id: String,
        var name: String,
        var itemId: Int,
        var qualityId: Int,
        var clientId: Int,
        var icon: String?,
        var isGround: Boolean,
        var isFlying: Boolean,
        var isAquatic: Boolean ,
        var isCollected: Boolean) {
    /**
     * generated
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Mount
        if (id != other.id) return false
        if (name != other.name) return false
        if (itemId != other.itemId) return false
        if (qualityId != other.qualityId) return false
        if (clientId != other.clientId) return false
        if (icon != other.icon) return false
        if (isGround != other.isGround) return false
        if (isFlying != other.isFlying) return false
        if (isAquatic != other.isAquatic) return false
        if (isCollected != other.isCollected) return false
        return true
    }

    /**
     * generated
     */
    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + itemId
        result = 31 * result + qualityId
        result = 31 * result + clientId
        result = 31 * result + (icon?.hashCode() ?: 0)
        result = 31 * result + isGround.hashCode()
        result = 31 * result + isFlying.hashCode()
        result = 31 * result + isAquatic.hashCode()
        result = 31 * result + isCollected.hashCode()
        return result
    }


    fun getIconUrl() = if (icon != null) "https://render-us.worldofwarcraft.com/icons/56/$icon.jpg" else null

    fun getImageUrl() = if (clientId != 0) "https://render-us.worldofwarcraft.com/npcs/zoom/creature-display-$clientId.jpg" else null
}