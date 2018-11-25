package com.riningan.wowmount.ui.mounts

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.riningan.frarg.processor.FragmentBuilder

class PagerAdapter constructor(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    override fun getCount() = 4

    override fun getItem(position: Int): Fragment {
        return FragmentBuilder.newMountsListFragmentInstance(position)
    }
}