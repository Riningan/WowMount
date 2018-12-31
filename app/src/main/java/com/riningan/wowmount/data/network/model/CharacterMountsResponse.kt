package com.riningan.wowmount.data.network.model

import com.google.gson.annotations.SerializedName


data class CharacterMountsResponse(
        @SerializedName("numCollected")
        var numCollected: Int,
        @SerializedName("numNotCollected")
        var numNotCollected: Int,
        @SerializedName("collected")
        var collected: List<MountResponse>)