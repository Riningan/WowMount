package com.riningan.wowmount.ui.mounts

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.riningan.frarg.annotations.ArgumentedFragment
import com.riningan.wowmount.R
import com.riningan.wowmount.data.model.Character
import com.riningan.wowmount.data.model.Mount
import com.riningan.wowmount.ui.base.BaseFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_mounts.*
import org.kodein.di.generic.instance


@ArgumentedFragment
class MountsFragment : BaseFragment(), MountsView {
    private val presenter: MountsPresenter by instance()

    @InjectPresenter(tag = MountsPresenter.TAG, type = PresenterType.WEAK)
    lateinit var mPresenter: MountsPresenter

    @ProvidePresenter(tag = MountsPresenter.TAG, type = PresenterType.WEAK)
    fun providePresenter() = presenter

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
        mPresenter.loadCharacter()
    }

    override fun onStop() {
        super.onStop()
        mPresenter.clearSubscriptions()
    }

    override fun setCharacter(character: Character) {
        Picasso.get()
                .load(character.getMainUrl())
                .into(ivCharacter)
        tlbMounts.setTitle(character.name)
    }

    override fun setMounts(mounts: List<Mount>) {
        /**
         * @see PageFragment
         */
    }

    override fun showErrorDialog(message: String) {
        // called from presenter, view is exists
        Snackbar.make(view!!, message, Snackbar.LENGTH_LONG)
    }
}