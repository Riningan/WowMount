package com.riningan.wowmount.ui.mount

import android.os.Build
import android.os.Bundle
import android.support.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.riningan.wowmount.R
import com.riningan.wowmount.data.repository.model.Mount
import com.riningan.wowmount.ui.base.BaseFragment
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_mount.*


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tlbMount.setNavigationOnClickListener { mPresenter.onBackClick() }
        mPresenter.setupTransitionView()
    }

    override fun onStart() {
        super.onStart()
        mPresenter.setupMount()
    }

    override fun onStop() {
        super.onStop()
        mPresenter.clearSubscriptions()
    }

    override fun setTransition(transitionName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ivMountIcon.transitionName = transitionName
        }
    }

    override fun setMount(mount: Mount) {
        Picasso.get()
                .load(mount.getIconUrl())
                .noFade()
                .into(ivMountIcon, object : Callback {
                    override fun onSuccess() {
                        startPostponedEnterTransition()
                    }

                    override fun onError(e: Exception?) {
                        startPostponedEnterTransition()
                    }
                })
        tvMountName.text = mount.name
    }

    override fun showError() {
    }
}