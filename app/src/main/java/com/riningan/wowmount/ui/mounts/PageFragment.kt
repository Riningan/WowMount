package com.riningan.wowmount.ui.mounts

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.riningan.frarg.FrargBinder
import com.riningan.frarg.annotations.Argument
import com.riningan.frarg.annotations.ArgumentedFragment
import com.riningan.wowmount.R
import com.riningan.wowmount.data.repository.model.Character
import com.riningan.wowmount.data.repository.model.Mount
import kotlinx.android.synthetic.main.page_mounts.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.support.closestKodein
import org.kodein.di.generic.instance


@ArgumentedFragment(alias = "MountsListFragment")
class PageFragment : MvpAppCompatFragment(), KodeinAware, MountsView, ItemsAdapter.OnAdapterListener {
    @Argument
    private lateinit var mType: MountTypes
    private var mClickedIcon: View? = null

    @InjectPresenter(tag = MountsPresenter.TAG, type = PresenterType.WEAK)
    lateinit var mPresenter: MountsPresenter

    override val kodein by closestKodein()

    @ProvidePresenter(tag = MountsPresenter.TAG, type = PresenterType.WEAK)
    fun providePresenter(): MountsPresenter {
        val presenter by kodein.instance<MountsPresenter>()
        return presenter
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        FrargBinder.bind(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.page_mounts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvMounts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ItemsAdapter(mType, this@PageFragment)
            addItemDecoration(MountsDecorator())
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    when (newState) {
                        RecyclerView.SCROLL_STATE_IDLE -> mPresenter.onMountsStopScrolling()
                        else -> mPresenter.onMountsStartScrolling()
                    }
                }
            })
        }
    }

    override fun setCharacter(character: Character) {
        /**
         * @see MountsFragment
         */
    }

    override fun setMounts(mounts: List<Mount>) {
        (rvMounts.adapter as ItemsAdapter).apply {
            val curMounts = when (mType) {
                MountTypes.ALL -> mounts
                MountTypes.GROUND -> mounts.filter { it.isGround }
                MountTypes.FLYING -> mounts.filter { it.isFlying }
                MountTypes.AQUATIC -> mounts.filter { it.isAquatic }
            }
            val diffResult = DiffUtil.calculateDiff(MountDiffUtilCallback(getMounts(), curMounts))
            setMounts(curMounts)
            diffResult.dispatchUpdatesTo(this)
        }
    }

    override fun showRequestErrorDialog(message: String) {
        /**
         * @see MountsFragment
         */
    }

    override fun showLogoutErrorDialog() {
        /**
         * @see MountsFragment
         */
    }

    override fun setPagerSwipeEnable(isEnabled: Boolean) {
        /**
         * @see MountsFragment
         */
    }

    override fun onClick(mount: Mount, view: View) {
        mClickedIcon = view
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mPresenter.onMountClick(view.transitionName, mount)
        } else {
            mPresenter.onMountClick("", mount)
        }
    }

    override fun stopRefresh() {
        /**
         * @see MountsFragment
         */
    }


    fun getIconView(): View? = mClickedIcon


    enum class MountTypes {
        ALL, GROUND, FLYING, AQUATIC
    }
}