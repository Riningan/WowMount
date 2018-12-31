package com.riningan.wowmount.data.network.model

import com.google.gson.annotations.SerializedName


data class MountsResponse(
        @SerializedName("mounts")
        var mounts: List<MountResponse>)