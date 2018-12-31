package com.riningan.wowmount.data.network.model

import com.google.gson.annotations.SerializedName


data class AccessTokenResponse(
        @SerializedName("access_token")
        var accessToken: String,
        @SerializedName("token_type")
        var tokenType: String,
        @SerializedName("expires_in")
        var expiresIn: Int
)