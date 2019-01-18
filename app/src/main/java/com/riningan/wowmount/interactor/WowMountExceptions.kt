package com.riningan.wowmount.interactor

import com.riningan.wowmount.R
import com.riningan.wowmount.app.WowMountApp


sealed class WowMountExceptions : Throwable() {
    override fun getLocalizedMessage(): String {
        return getApplicationMessage(this)
    }


    class IOException : WowMountExceptions()

    class NoInternetException : WowMountExceptions()

    class AuthorizedException : WowMountExceptions()

    class ApplicationException : WowMountExceptions()


    companion object {
        private fun getApplicationMessage(e: WowMountExceptions): String = WowMountApp.getContext().getString(
                when (e) {
                    is IOException -> R.string.error_character_io
                    is NoInternetException -> R.string.error_character_no_internet
                    is AuthorizedException -> R.string.error_authorized
                    is ApplicationException -> R.string.error_character_application
                })
    }
}