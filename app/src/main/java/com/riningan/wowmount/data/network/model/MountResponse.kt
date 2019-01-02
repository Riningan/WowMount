package com.riningan.wowmount.data.network.model

import com.google.gson.annotations.SerializedName


data class MountResponse(
        @SerializedName("name")
        var name: String,
        @SerializedName("spellId")
        var spellId: Int,
        @SerializedName("creatureId")
        var creatureId: Int,
        @SerializedName("itemId")
        var itemId: Int,
        @SerializedName("qualityId")
        var qualityId: Int,
        @SerializedName("icon")
        var icon: String,
        @SerializedName("isGround")
        var isGround: Boolean,
        @SerializedName("isFlying")
        var isFlying: Boolean,
        @SerializedName("isAquatic")
        var isAquatic: Boolean,
        @SerializedName("isJumping")
        var isJumping: Boolean
)