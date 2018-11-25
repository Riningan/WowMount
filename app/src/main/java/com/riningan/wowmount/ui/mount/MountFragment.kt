package com.riningan.wowmount.ui.mount

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.riningan.wowmount.R
import com.riningan.wowmount.ui.base.BaseFragment

class MountFragment :BaseFragment(), MountView {
    @InjectPresenter
    lateinit var mPresenter: MountPresenter

    @ProvidePresenter
    fun provideDialogPresenter() = MountPresenter(kodein)

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mPresenter.bind(arguments!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mount, container, false)
    }
}