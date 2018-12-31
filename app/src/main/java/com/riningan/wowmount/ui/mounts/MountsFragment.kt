package com.riningan.wowmount.ui.mounts

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.riningan.frarg.annotations.ArgumentedFragment
import com.riningan.widget.ExtendedViewPager
import com.riningan.wowmount.R
import com.riningan.wowmount.data.repository.model.Character
import com.riningan.wowmount.data.repository.model.Mount
import com.riningan.wowmount.ui.base.BaseFragment
import com.riningan.wowmount.utils.SnackbarUtil
import com.riningan.wowmount.utils.onPageScrollStateChanged
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
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
        srlMounts.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary)
        srlMounts.setOnRefreshListener { mPresenter.onRefresh() }
        vpMounts.adapter = PagerAdapter(childFragmentManager)
        vpMounts.onPageScrollStateChanged { state ->
            when (state) {
                ViewPager.SCROLL_STATE_IDLE -> srlMounts.isEnabled = true
                else -> srlMounts.isEnabled = false
            }
        }
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
        SnackbarUtil.showError(view, message)
    }

    override fun setPagerSwipeEnable(isEnabled: Boolean) {
        vpMounts.setAllowedSwipeDirection(if (isEnabled) ExtendedViewPager.SwipeDirection.All else ExtendedViewPager.SwipeDirection.NONE)
    }

    override fun stopRefresh() {
        srlMounts.isRefreshing = false
    }


    fun getIconForAnimation(): View? =
            (childFragmentManager.findFragmentByTag("android:switcher:" + R.id.vpMounts + ":" + vpMounts.currentItem) as? PageFragment)?.getIconView()
}