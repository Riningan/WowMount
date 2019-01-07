package com.riningan.wowmount.data.preferences

import android.content.Context


class AppPreferences constructor(context: Context) : LocalPreferences {
    private val mSharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)


    override var isActivated: Boolean
        get() = mSharedPreferences.getBoolean(IS_ACTIVATED, false)
        set(value) = mSharedPreferences.edit().putBoolean(IS_ACTIVATED, value).apply()

    override var server: String
        get() = mSharedPreferences.getString(SERVER, "")!!
        set(value) = mSharedPreferences.edit().putString(SERVER, value).apply()

    override var realmName: String
        get() = mSharedPreferences.getString(REALM_NAME, "")!!
        set(value) = mSharedPreferences.edit().putString(REALM_NAME, value).apply()

    override var characterName: String
        get() = mSharedPreferences.getString(CHARACTER_NAME, "")!!
        set(value) = mSharedPreferences.edit().putString(CHARACTER_NAME, value).apply()

    override var accessToken: String
        get() = mSharedPreferences.getString(ACCESS_TOKEN, "")!!
        set(value) = mSharedPreferences.edit().putString(ACCESS_TOKEN, value).apply()

    override var showAll: Boolean
        get() = mSharedPreferences.getBoolean(SHOW_ALL, true)
        set(value) = mSharedPreferences.edit().putBoolean(SHOW_ALL, value).apply()


    override fun clear() {
        mSharedPreferences.edit().apply {
            putBoolean(IS_ACTIVATED, false)
            putString(SERVER, "")
            putString(REALM_NAME, "")
            putString(CHARACTER_NAME, "")
        }.apply()
    }


    companion object {
        private const val PREFERENCES = "PREFERENCES"
        private const val IS_ACTIVATED = "IS_ACTIVATED"
        private const val SERVER = "SERVER"
        private const val CHARACTER_NAME = "CHARACTER_NAME"
        private const val REALM_NAME = "REALM_NAME"
        private const val ACCESS_TOKEN = "ACCESS_TOKEN"
        private const val SHOW_ALL = "SHOW_ALL"
    }
}