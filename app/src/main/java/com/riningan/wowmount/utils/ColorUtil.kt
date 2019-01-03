package com.riningan.wowmount.utils

import android.os.Build
import com.riningan.wowmount.app.WowMountApp


object ColorUtil {
    @Suppress("DEPRECATION")
    fun getColor(resourceId: Int) = WowMountApp.getContext().resources.run {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getColor(resourceId, null)
        } else {
            getColor(resourceId)
        }
    }
}