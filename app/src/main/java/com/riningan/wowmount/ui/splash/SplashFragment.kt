package com.riningan.wowmount.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.riningan.frarg.annotations.ArgumentedFragment
import com.riningan.wowmount.R
import com.riningan.wowmount.ui.base.BaseFragment
import com.riningan.wowmount.utils.SnackbarUtil
import kotlinx.android.synthetic.main.fragment_splash.*


@ArgumentedFragment
class SplashFragment : BaseFragment(), SplashView {
    @InjectPresenter
    lateinit var mPresenter: SplashPresenter

    @ProvidePresenter
    fun providePresenter() = SplashPresenter(kodein)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_splash, container, false)

    override fun onStart() {
        super.onStart()
        mPresenter.onStart()
    }

    override fun onStop() {
        super.onStop()
        mPresenter.clearSubscriptions()
    }

    override fun showErrorDialog(message: String) {
        SnackbarUtil.showError(view, message)
    }


    fun getLogoForAnimation(): ImageView = ivLogo
}