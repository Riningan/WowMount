package com.riningan.wowmount.ui.base

import android.content.Context
import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.MvpAppCompatFragment
import com.riningan.util.Logger
import com.riningan.wowmount.BuildConfig
import com.riningan.wowmount.app.WowMountApp
import com.riningan.wowmount.utils.LogUtil
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinTrigger
import org.kodein.di.android.support.closestKodein


open class BaseFragment : MvpAppCompatFragment(), BaseView, KodeinAware {
    override val kodein by closestKodein()
    override val kodeinTrigger = KodeinTrigger()

    override fun onAttach(context: Context?) {
        Logger.forThis(this).debug()
        LogUtil.addDebug(this)
        super.onAttach(context)
        kodeinTrigger.trigger()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Logger.forThis(this).debug()
        LogUtil.addDebug(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Logger.forThis(this).debug()
        LogUtil.addDebug(this)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        Logger.forThis(this).debug()
        LogUtil.addDebug(this)
        super.onStart()
    }

    override fun onResume() {
        Logger.forThis(this).debug()
        LogUtil.addDebug(this)
        super.onResume()
    }

    override fun onPause() {
        Logger.forThis(this).debug()
        LogUtil.addDebug(this)
        super.onPause()
    }

    override fun onStop() {
        Logger.forThis(this).debug()
        LogUtil.addDebug(this)
        super.onStop()
    }

    override fun onDestroyView() {
        Logger.forThis(this).debug()
        LogUtil.addDebug(this)
        super.onDestroyView()
    }

    override fun onDestroy() {
        Logger.forThis(this).debug()
        LogUtil.addDebug(this)
        // LeakCanary
        if (BuildConfig.DEBUG) {
            activity?.let { WowMountApp.getRefWatcher().watch(this) }
        }
        super.onDestroy()
    }
}