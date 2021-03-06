package com.riningan.wowmount.presentation.ui.mount

import android.os.Build
import android.os.Bundle
import android.support.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.riningan.frarg.FrargBinder
import com.riningan.wowmount.R
import com.riningan.wowmount.data.repository.model.Mount
import com.riningan.wowmount.presentation.ui.base.BaseFragment
import com.riningan.wowmount.utils.SnackbarUtil
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.GrayscaleTransformation
import kotlinx.android.synthetic.main.fragment_mount.*


class MountFragment : BaseFragment(), MountView {
    @InjectPresenter
    lateinit var mPresenter: MountPresenter


    @ProvidePresenter
    fun providePresenter() = MountPresenter(kodein)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FrargBinder.bind(mPresenter, arguments!!)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            postponeEnterTransition()
        }
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
                .apply {
                    if (!mount.isCollected) {
                        transform(GrayscaleTransformation())
                    }
                }
                .noFade()
                .into(ivMountIcon, object : Callback {
                    override fun onSuccess() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startPostponedEnterTransition()
                        }
                    }

                    override fun onError(e: Exception?) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startPostponedEnterTransition()
                        }
                    }
                })
        tvMountName.text = mount.name
        if (mount.getImageUrl() == null) {
            ivMount.setImageResource(R.drawable.ic_no_image)
        } else {
            Picasso.get()
                    .load(mount.getImageUrl())
                    .error(R.drawable.ic_no_image)
                    .into(ivMount)
        }
    }

    override fun showError(message: String) {
        SnackbarUtil.showError(activity, message)
    }
}