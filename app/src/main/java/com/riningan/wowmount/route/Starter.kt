package com.riningan.wowmount.route

import com.riningan.wowmount.ui.splash.SplashFragment


class Starter {
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun getStartFragmentId(): String = SplashFragment::class.java.canonicalName
}