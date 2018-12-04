package com.riningan.wowmount.ui.splash

import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.riningan.frarg.annotations.ArgumentedFragment
import com.riningan.wowmount.R
import com.riningan.wowmount.ui.base.BaseFragment
import com.riningan.wowmount.utils.ColorUtil
import kotlinx.android.synthetic.main.fragment_splash.*


@ArgumentedFragment
class SplashFragment : BaseFragment(), SplashView {
    @InjectPresenter
    lateinit var mPresenter: SplashPresenter

    @ProvidePresenter
    fun providePresenter() = SplashPresenter(kodein)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ivLogo.transitionName = LOGO_TRANSITION
        }
    }

    override fun onStart() {
        super.onStart()
        mPresenter.onStart()
    }

    override fun onStop() {
        super.onStop()
        mPresenter.clearSubscriptions()
    }

    override fun showErrorDialog(message: String) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_LONG).apply {
                view.setBackgroundResource(R.color.colorError)
                setActionTextColor(ColorUtil.getColor(R.color.colorOnError))
            }.show()
        }
    }


    fun getLogoForAnimation(): ImageView = ivLogo!!


    companion object {
        const val LOGO_TRANSITION = "LOGO_TRANSITION"
    }
}