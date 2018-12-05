package com.riningan.wowmount.ui.mount

import android.os.Build
import android.os.Bundle
import android.support.transition.TransitionInflater
import android.support.v4.view.ViewCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.riningan.wowmount.R
import com.riningan.wowmount.data.model.Mount
import com.riningan.wowmount.ui.base.BaseFragment
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_mount.*
import java.lang.Exception


class MountFragment : BaseFragment(), MountView {
    @InjectPresenter
    lateinit var mPresenter: MountPresenter

    @ProvidePresenter
    fun providePresenter() = MountPresenter(kodein)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.bind(arguments!!)
        postponeEnterTransition()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        }
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

    override fun setTransitionName(transitionName: String) {
        ViewCompat.setTransitionName(ivMountIcon, transitionName)
    }

    override fun showMount(mount: Mount) {
        Picasso.get()
                .load(mount.getIconUrl())
                .into(ivMountIcon, object : Callback {
                    override fun onSuccess() {
                        startPostponedEnterTransition()
                    }

                    override fun onError(e: Exception?) {
                        startPostponedEnterTransition()
                    }
                })
    }

    override fun showError() {
    }
}