package com.riningan.wowmount.ui.mounts

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.riningan.frarg.FrargBinder
import com.riningan.frarg.annotations.Argument
import com.riningan.frarg.annotations.ArgumentedFragment
import com.riningan.wowmount.R
import org.kodein.di.KodeinAware
import org.kodein.di.android.support.closestKodein
import org.kodein.di.generic.instance


@ArgumentedFragment(alias = "MountsListFragment")
class PageFragment : MvpAppCompatFragment() {
    @Argument
    private var mType: Int = 0

    @InjectPresenter(tag = MountsPresenter.TAG, type = PresenterType.WEAK)
    lateinit var mPresenter: MountsPresenter


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        FrargBinder.bind(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.page_mounts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    enum class MountTypes {
        ALL, GROUND, FLYING, AQUATIC
    }
}