package com.riningan.wowmount.ui.mounts

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
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
import com.riningan.wowmount.data.model.Character
import com.riningan.wowmount.data.model.Mount
import kotlinx.android.synthetic.main.page_mounts.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.support.closestKodein
import org.kodein.di.generic.instance


@ArgumentedFragment(alias = "MountsListFragment")
class PageFragment : MvpAppCompatFragment(), KodeinAware, MountsView, ItemsAdapter.OnAdapterListener {
    @Argument
    private var mType: Int = 0

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
        rvMounts.layoutManager = LinearLayoutManager(context)
        rvMounts.adapter = ItemsAdapter(this)
    }

    override fun setCharacter(character: Character) {
        /**
         * @see MountsFragment
         */
    }

    override fun setMounts(mounts: List<Mount>) {
        val type = MountTypes.values()[mType]
        val curMounts = when (type) {
            MountTypes.ALL -> mounts
            MountTypes.GROUND -> mounts.filter { it.isGround }
            MountTypes.FLYING -> mounts.filter { it.isFlying }
            MountTypes.AQUATIC -> mounts.filter { it.isAquatic }
        }
        (rvMounts.adapter as ItemsAdapter).apply {
            setMounts(curMounts)
            notifyDataSetChanged()
        }
    }

    override fun showErrorDialog(message: String) {
        /**
         * @see MountsFragment
         */
    }

    override fun onClick(mount: Mount) {
        mPresenter.onMountClick(mount)
    }


    enum class MountTypes {
        ALL, GROUND, FLYING, AQUATIC
    }
}