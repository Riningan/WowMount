package com.riningan.wowmount.ui.mounts

import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.riningan.wowmount.R
import com.riningan.wowmount.data.model.Mount
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_mount.view.*


class ItemsAdapter constructor(private val mFragment: Fragment, private val mListener: OnAdapterListener) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {
    private val mMounts = arrayListOf<Mount>()


    override fun getItemCount() = mMounts.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_mount, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mMounts[position])
    }


    fun setMounts(mounts: List<Mount>) {
        mMounts.clear()
        mMounts.addAll(mounts)
    }


    inner class ViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        private var mMount: Mount? = null


        init {
            view.setOnClickListener {
                mMount?.let { mount ->
                    mListener.onClick(mount)
                }
            }
        }


        fun bind(mount: Mount) {
            mMount = mount
            Picasso.get()
                    .load(mount.getIconUrl())
                    .into(itemView.ivMountIcon)
            itemView.tvMountName.text = mount.name
            itemView.tvMountDesc.text = ""
        }
    }


    interface OnAdapterListener {
        fun onClick(mount: Mount)
    }
}