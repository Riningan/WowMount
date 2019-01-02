package com.riningan.wowmount.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.riningan.frarg.annotations.ArgumentedFragment
import com.riningan.wowmount.R
import com.riningan.wowmount.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_about.*


@ArgumentedFragment
class AboutFragment : BaseFragment(), AboutView {
    @InjectPresenter
    lateinit var mPresenter: AboutPresenter

    @ProvidePresenter
    fun providePresenter() = AboutPresenter(kodein)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_about, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tlbAbout.setNavigationOnClickListener { mPresenter.onBackClick() }
    }
}