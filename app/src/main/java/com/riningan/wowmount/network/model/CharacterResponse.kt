package com.riningan.wowmount.network.model

import com.google.gson.annotations.SerializedName


data class CharacterResponse(
        @SerializedName("lastModified")
        var lastModified: Long = Long.MAX_VALUE,
        @SerializedName("name")
        var name: String,
        @SerializedName("realm")
        var realm: String,
        @SerializedName("battlegroup")
        var battlegroup: String,
        @SerializedName("class")
        var cls: Int,
        @SerializedName("race")
        var race: Int,
        @SerializedName("gender")
        var gender: Int,
        @SerializedName("level")
        var level: Int,
        @SerializedName("achievementPoints")
        var achievementPoints: Int,
        @SerializedName("thumbnail")
        var thumbnail: String,
        @SerializedName("calcClass")
        var calcClass: String,
        @SerializedName("faction")
        var faction: Int,
        @SerializedName("mounts")
        var mounts: CharacterMountsResponse,
        @SerializedName("totalHonorableKills")
        var totalHonorableKills: Int
)