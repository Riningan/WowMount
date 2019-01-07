package com.riningan.wowmount.utils

import android.app.Activity
import android.support.design.widget.Snackbar
import com.riningan.wowmount.R
import com.riningan.wowmount.app.WowMountApp


object SnackbarUtil {
    fun showError(activity: Activity?, resourceId: Int) {
        showError(activity, WowMountApp.getContext().getString(resourceId))
    }

    fun showError(activity: Activity?, message: String) {
        activity?.let {
            Snackbar.make(it.window.decorView, message, Snackbar.LENGTH_LONG).apply {
                view.setBackgroundResource(R.color.colorError)
                setActionTextColor(ColorUtil.getColor(R.color.colorOnError))
            }.show()
        }
    }
}