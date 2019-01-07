package com.riningan.wowmount.ui.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.riningan.frarg.FrargBinder
import com.riningan.wowmount.R
import com.riningan.wowmount.ui.base.BaseFragment
import com.riningan.wowmount.utils.SnackbarUtil
import kotlinx.android.synthetic.main.fragment_filter.*


class FilterFragment : BaseFragment(), FilterView, CompoundButton.OnCheckedChangeListener {
    @InjectPresenter
    lateinit var mPresenter: FilterPresenter

    @ProvidePresenter
    fun providePresenter() = FilterPresenter(kodein)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FrargBinder.bind(mPresenter, arguments!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_filter, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tlbFilter.setNavigationOnClickListener { mPresenter.onBackClick() }
        btnFilterShow.setOnClickListener { mPresenter.onShowClick() }
        mPresenter.onViewCreated()
    }

    override fun onStart() {
        super.onStart()
        mPresenter.onStart()
    }

    override fun onStop() {
        super.onStop()
        mPresenter.clearSubscriptions()
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        mPresenter.onShowAllChecked(isChecked)
    }

    override fun setFilter(showAll: Boolean) {
        cbFilterShowAll.apply {
            setOnCheckedChangeListener(null)
            isChecked = showAll
            setOnCheckedChangeListener(this@FilterFragment)
        }
    }

    override fun showButton(count: Int) {
        pbFilter.visibility = View.INVISIBLE
        btnFilterShow.apply {
            visibility = View.VISIBLE
            text = getString(R.string.filter_show_with_count, count)
        }
    }

    override fun showProgress() {
        pbFilter.visibility = View.VISIBLE
        btnFilterShow.visibility = View.INVISIBLE
    }

    override fun showError(message: String) {
        SnackbarUtil.showError(view, message)
        pbFilter.visibility = View.INVISIBLE
        btnFilterShow.apply {
            visibility = View.VISIBLE
            text = getString(R.string.filter_show_without_count)
        }
    }
}