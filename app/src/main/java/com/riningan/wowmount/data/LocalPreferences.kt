package com.riningan.wowmount.data

import android.content.Context

class LocalPreferences constructor(context: Context) {
    private val mSharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)


    fun getServer() : String? = mSharedPreferences.getString(SERVER, null)

    fun setServer(server : String) {
        mSharedPreferences.edit().putString(SERVER, server).apply()
    }


    fun getCharacterName() : String? = mSharedPreferences.getString(CHARACTER_NAME, null)

    fun setCharacterName(characterName : String) {
        mSharedPreferences.edit().putString(CHARACTER_NAME, characterName).apply()
    }


    fun getRealmName() : String? = mSharedPreferences.getString(REALM_NAME, null)

    fun setRealmName(realm : String) {
        mSharedPreferences.edit().putString(REALM_NAME, realm).apply()
    }


    fun getAccessToken() : String? = mSharedPreferences.getString(ACCESS_TOKEN, null)

    fun setAccessToken(accessToken : String) {
        mSharedPreferences.edit().putString(ACCESS_TOKEN, accessToken).apply()
    }


    companion object {
        private const val PREFERENCES = "PREFERENCES"
        private const val SERVER = "SERVER"
        private const val CHARACTER_NAME = "CHARACTER_NAME"
        private const val REALM_NAME = "REALM_NAME"
        private const val ACCESS_TOKEN = "ACCESS_TOKEN"
    }
}