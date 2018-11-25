package com.riningan.wowmount.network.model

import com.google.gson.annotations.SerializedName

data class MountsResponse(
        @SerializedName("numCollected")
        var numCollected: Int,
        @SerializedName("numNotCollected")
        var numNotCollected: Int,
        @SerializedName("collected")
        var collected: List<MountResponse>)