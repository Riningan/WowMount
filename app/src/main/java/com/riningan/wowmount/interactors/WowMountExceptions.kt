package com.riningan.wowmount.interactors


sealed class WowMountExceptions : Throwable() {
    class IOException : WowMountExceptions()
    class NoInternetException : WowMountExceptions()
    class AuthorizedException : WowMountExceptions()
    class ApplicationException : WowMountExceptions()
}