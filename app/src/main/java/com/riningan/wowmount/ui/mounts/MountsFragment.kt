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
import com.riningan.wowmount.widget.ControlledViewPager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_mounts.*
import org.kodein.di.generic.instance


@ArgumentedFragment
class MountsFragment : BaseFragment(), MountsView {
    @InjectPresenter(tag = MountsPresenter.TAG, type = PresenterType.WEAK)
    lateinit var mPresenter: MountsPresenter

    @ProvidePresenter(tag = MountsPresenter.TAG, type = PresenterType.WEAK)
    fun providePresenter(): MountsPresenter {
        val presenter by kodein.instance<MountsPresenter>()
        return presenter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_mounts, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tlbMounts.inflateMenu(R.menu.menu_mounts)
        tlbMounts.setOnMenuItemClickListener { menu ->
            when (menu.itemId) {
                R.id.miLogout -> mPresenter.onLogoutClick()
            }
            true
        }
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
        tlbMounts.title = character.name
    }

    override fun setMounts(mounts: List<Mount>) {
        /**
         * @see PageFragment
         */
    }

    override fun showErrorDialog(message: String) {
        view?.run {
            Snackbar.make(this, message, Snackbar.LENGTH_LONG)
        }
    }

    override fun setPagerSwipeEnable(isEnabled: Boolean) {
        vpMounts.setAllowedSwipeDirection(if (isEnabled) ControlledViewPager.SwipeDirection.All else ControlledViewPager.SwipeDirection.NONE)
    }


    fun getIconForAnimation(): View? =
            (childFragmentManager.findFragmentByTag("android:switcher:" + R.id.vpMounts + ":" + vpMounts.currentItem) as? PageFragment)?.getIconView()
}