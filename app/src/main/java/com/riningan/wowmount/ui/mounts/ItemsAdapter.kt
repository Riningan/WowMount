package com.riningan.wowmount.ui.mounts

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.riningan.wowmount.R
import com.riningan.wowmount.data.model.Mount


class ItemsAdapter constructor(private val mListener: OnListener) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {


    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_mount, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind()
    }


    inner class ViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        private var mMount: Mount? = null


        init {
            view.setOnClickListener {
                mMount?.let { mount ->
                    mListener.onClickListener(mount)
                }
            }
        }


        fun bind(mount: Mount) {
            mMount = mount

        }
    }


    interface OnListener {
        fun onClickListener(mount: Mount)
    }
}