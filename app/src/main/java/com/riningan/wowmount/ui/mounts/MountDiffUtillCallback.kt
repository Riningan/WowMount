package com.riningan.wowmount.ui.mounts

import android.support.v7.util.DiffUtil
import com.riningan.wowmount.data.repository.model.Mount


class MountDiffUtillCallback constructor(private val mOld: List<Mount>, private val mNew: List<Mount>) : DiffUtil.Callback() {
    override fun getOldListSize() = mOld.size

    override fun getNewListSize() = mNew.size

    override fun areItemsTheSame(oldPosition: Int, newPosition: Int) = mOld[oldPosition].id == mNew[newPosition].id

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int) = mOld[oldPosition].id == mNew[newPosition].id
}