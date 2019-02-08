package com.riningan.wowmount.data.preferences

import android.content.Context
import android.os.StrictMode


class AppPreferences constructor(context: Context) : LocalPreferences {
    private val mSharedPreferences = allow { context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE) }


    override var isActivated: Boolean
        get() = allow { mSharedPreferences.getBoolean(IS_ACTIVATED, false) }
        set(value) = allow { mSharedPreferences.edit().putBoolean(IS_ACTIVATED, value).apply() }

    override var server: String
        get() = allow { mSharedPreferences.getString(SERVER, "")!! }
        set(value) = allow { mSharedPreferences.edit().putString(SERVER, value).apply() }

    override var realmName: String
        get() = allow { mSharedPreferences.getString(REALM_NAME, "")!! }
        set(value) = allow { mSharedPreferences.edit().putString(REALM_NAME, value).apply() }

    override var characterName: String
        get() = allow { mSharedPreferences.getString(CHARACTER_NAME, "")!! }
        set(value) = allow { mSharedPreferences.edit().putString(CHARACTER_NAME, value).apply() }

    override var accessToken: String
        get() = allow { mSharedPreferences.getString(ACCESS_TOKEN, "")!! }
        set(value) = allow { mSharedPreferences.edit().putString(ACCESS_TOKEN, value).apply() }

    override var showAll: Boolean
        get() = allow { mSharedPreferences.getBoolean(SHOW_ALL, true) }
        set(value) = allow { mSharedPreferences.edit().putBoolean(SHOW_ALL, value).apply() }


    override fun clear() {
        allow {
            mSharedPreferences.edit().apply {
                putBoolean(IS_ACTIVATED, false)
                putString(SERVER, "")
                putString(REALM_NAME, "")
                putString(CHARACTER_NAME, "")
            }.apply()
        }
    }


    private fun <T> allow(block: () -> T): T {
        val oldPolicy = StrictMode.allowThreadDiskReads()
        try {
            return block()
        } finally {
            StrictMode.setThreadPolicy(oldPolicy)
        }
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