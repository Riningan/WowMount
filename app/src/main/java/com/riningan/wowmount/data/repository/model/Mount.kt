package com.riningan.wowmount.data.repository.model


data class Mount(
        var id: String = "",
        var name: String = "",
        var itemId: Int = 0,
        var qualityId: Int = 0,
        var icon: String? = null,
        var isGround: Boolean = false,
        var isFlying: Boolean = false,
        var isAquatic: Boolean = false,
        var isCollected: Boolean = false) {
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
        result = 31 * result + (icon?.hashCode() ?: 0)
        result = 31 * result + isGround.hashCode()
        result = 31 * result + isFlying.hashCode()
        result = 31 * result + isAquatic.hashCode()
        result = 31 * result + isCollected.hashCode()
        return result
    }


    fun getIconUrl() = if (icon != null) "https://render-us.worldofwarcraft.com/icons/56/$icon.jpg" else null
}