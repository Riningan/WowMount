package com.riningan.wowmount.data.preferences


interface LocalPreferences {
    var isActivated: Boolean
    var server: String
    var realmName: String
    var characterName: String
    var accessToken: String

    fun clear()
}