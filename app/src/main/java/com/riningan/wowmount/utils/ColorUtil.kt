package com.riningan.wowmount.utils

import android.os.Build
import com.riningan.wowmount.app.WowMountApp


object ColorUtil {
    @Suppress("DEPRECATION")
    fun getColor(resourceId: Int) : Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            WowMountApp.getContext().resources.getColor(resourceId, null)
        } else {
            WowMountApp.getContext().resources.getColor(resourceId)
        }
    }
}