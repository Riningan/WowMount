package com.riningan.wowmount.utils

import android.support.design.widget.Snackbar
import android.view.View
import com.riningan.wowmount.R
import com.riningan.wowmount.app.WowMountApp


object SnackbarUtil {
    fun showError(rootView: View?, resourceId: Int) {
        showError(rootView, WowMountApp.getContext().getString(resourceId))
    }

    fun showError(rootView: View?, message: String) {
        rootView?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_LONG).apply {
                view.setBackgroundResource(R.color.colorError)
                setActionTextColor(ColorUtil.getColor(R.color.colorOnError))
            }.show()
        }
    }
}