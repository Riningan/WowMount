package com.riningan.wowmount.ui.base

import android.content.Context
import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.MvpAppCompatFragment
import com.riningan.wowmount.utils.LogUtil
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinTrigger
import org.kodein.di.android.support.closestKodein


open class BaseFragment : MvpAppCompatFragment(), BaseView, KodeinAware {
    override val kodein by closestKodein()
    override val kodeinTrigger = KodeinTrigger()

    override fun onAttach(context: Context?) {
        LogUtil.addDebug(this)
        super.onAttach(context)
        kodeinTrigger.trigger()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        LogUtil.addDebug(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        LogUtil.addDebug(this)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        LogUtil.addDebug(this)
        super.onStart()
    }

    override fun onResume() {
        LogUtil.addDebug(this)
        super.onResume()
    }

    override fun onPause() {
        LogUtil.addDebug(this)
        super.onPause()
    }

    override fun onStop() {
        LogUtil.addDebug(this)
        super.onStop()
    }

    override fun onDestroyView() {
        LogUtil.addDebug(this)
        super.onDestroyView()
    }

    override fun onDestroy() {
        LogUtil.addDebug(this)
        super.onDestroy()
    }
}