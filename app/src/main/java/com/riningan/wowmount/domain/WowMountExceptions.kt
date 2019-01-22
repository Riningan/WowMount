package com.riningan.wowmount.domain

import com.riningan.wowmount.R
import com.riningan.wowmount.app.WowMountApp
import com.riningan.wowmount.utils.DeviceUtil
import io.reactivex.exceptions.CompositeException
import retrofit2.HttpException


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


        fun throwableCast(it: Throwable): WowMountExceptions {
            val throwable = if (it is CompositeException) {
                it.exceptions[0]
            } else {
                it
            }
            return when {
                throwable is HttpException && throwable.code() == 401 -> AuthorizedException()
                throwable is java.io.IOException && DeviceUtil.isOnline() -> IOException()
                throwable is java.io.IOException -> NoInternetException()
                else -> ApplicationException()
            }
        }
    }
}