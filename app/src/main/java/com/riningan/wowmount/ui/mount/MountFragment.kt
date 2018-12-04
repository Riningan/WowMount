package com.riningan.wowmount.ui.mount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.riningan.wowmount.R
import com.riningan.wowmount.data.model.Mount
import com.riningan.wowmount.ui.base.BaseFragment


class MountFragment : BaseFragment(), MountView {
    @InjectPresenter
    lateinit var mPresenter: MountPresenter

    @ProvidePresenter
    fun providePresenter() = MountPresenter(kodein)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.bind(arguments!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_mount, container, false)

    override fun onStart() {
        super.onStart()
        mPresenter.onStart()
    }

    override fun onStop() {
        super.onStop()
        mPresenter.clearSubscriptions()
    }

    override fun showMount(mount: Mount) {
    }

    override fun showError() {
    }
}