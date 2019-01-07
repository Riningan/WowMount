package com.riningan.wowmount.utils

import android.content.Context
import android.net.ConnectivityManager
import com.riningan.wowmount.app.WowMountApp


object DeviceUtil {
    fun isOnline(): Boolean {
        val cm = WowMountApp.getContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val netInfo = cm!!.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }
}