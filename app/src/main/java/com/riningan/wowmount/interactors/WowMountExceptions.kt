package com.riningan.wowmount.interactors

import com.riningan.wowmount.R
import com.riningan.wowmount.app.WowMountApp


sealed class WowMountExceptions : Throwable() {
    class IOException : WowMountExceptions() {
        override fun getLocalizedMessage(): String {
            return WowMountApp.getContext().getString(R.string.error_character_io)
        }
    }

    class NoInternetException : WowMountExceptions() {
        override fun getLocalizedMessage(): String {
            return WowMountApp.getContext().getString(R.string.error_character_no_internet)
        }
    }

    class AuthorizedException : WowMountExceptions() {
        override fun getLocalizedMessage(): String {
            return WowMountApp.getContext().getString(R.string.error_authorized)
        }
    }

    class ApplicationException : WowMountExceptions() {
        override fun getLocalizedMessage(): String {
            return WowMountApp.getContext().getString(R.string.error_character_application)
        }
    }
}