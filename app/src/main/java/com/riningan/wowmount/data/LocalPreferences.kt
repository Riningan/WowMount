package com.riningan.wowmount.data

import android.content.Context

class LocalPreferences constructor(context: Context) {
    private val mSharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)


    var server: String
        get() = mSharedPreferences.getString(SERVER, "")!!
        set(value) = mSharedPreferences.edit().putString(SERVER, value).apply()

    var realmName: String
        get() = mSharedPreferences.getString(REALM_NAME, "")!!
        set(value) = mSharedPreferences.edit().putString(REALM_NAME, value).apply()

    var characterName: String
        get() = mSharedPreferences.getString(CHARACTER_NAME, "")!!
        set(value) = mSharedPreferences.edit().putString(CHARACTER_NAME, value).apply()

    var accessToken: String
        get() = mSharedPreferences.getString(ACCESS_TOKEN, "")!!
        set(value) = mSharedPreferences.edit().putString(ACCESS_TOKEN, value).apply()


    fun clear() {
        mSharedPreferences.edit().apply {
            putString(SERVER, "")
            putString(REALM_NAME, "")
            putString(CHARACTER_NAME, "")
        }.apply()
    }


    companion object {
        private const val PREFERENCES = "PREFERENCES"
        private const val SERVER = "SERVER"
        private const val CHARACTER_NAME = "CHARACTER_NAME"
        private const val REALM_NAME = "REALM_NAME"
        private const val ACCESS_TOKEN = "ACCESS_TOKEN"
    }
}