package com.riningan.wowmount.presentation.ui.mounts

import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.riningan.wowmount.R
import com.riningan.wowmount.data.repository.model.Mount
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_mount.view.*


class ItemsAdapter constructor(private val mType: PageFragment.MountTypes, private val mListener: OnAdapterListener) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {
    private val mMounts = arrayListOf<Mount>()


    override fun getItemCount() = mMounts.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_mount, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mMounts[position])
    }


    fun setMounts(mounts: List<Mount>) {
        mMounts.clear()
        mMounts.addAll(mounts)
    }

    fun getMounts() = mMounts


    interface OnAdapterListener {
        fun onClick(mount: Mount, view: View)
    }


    inner class ViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        private var mMount: Mount? = null


        init {
            itemView.setOnClickListener {
                mMount?.let { mount ->
                    mListener.onClick(mount, itemView.ivMountIcon)
                }
            }
        }


        fun bind(mount: Mount) {
            mMount = mount
            ViewCompat.setTransitionName(itemView.ivMountIcon, mType.name + "/" + mount.id)
            Picasso.get()
                    .load(mount.getIconUrl())
                    .into(itemView.ivMountIcon)
            itemView.tvMountName.apply {
                text = mount.name
                isEnabled = mount.isCollected
            }
        }
    }
}