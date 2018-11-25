package com.riningan.wowmount.ui.mounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.riningan.frarg.annotations.ArgumentedFragment
import com.riningan.wowmount.R
import com.riningan.wowmount.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_mounts.*


@ArgumentedFragment
class MountsFragment : BaseFragment(), MountsView {
    @InjectPresenter
    lateinit var mPresenter: MountsPresenter

    @ProvidePresenter
    fun provideDialogPresenter() = MountsPresenter(kodein)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mounts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tlMounts.setupWithViewPager(vpMounts)
        vpMounts.adapter = PagerAdapter(childFragmentManager)
    }

    override fun onStart() {
        super.onStart()
        mPresenter.onStart()
    }

    override fun onStop() {
        super.onStop()
        mPresenter.onStop()
    }
}