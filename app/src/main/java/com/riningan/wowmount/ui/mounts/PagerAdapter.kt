package com.riningan.wowmount.ui.mounts

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.riningan.frarg.processor.FragmentBuilder
import com.riningan.wowmount.R
import com.riningan.wowmount.app.WowMountApp


class PagerAdapter constructor(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    override fun getCount() = 4

    override fun getItem(position: Int): Fragment = FragmentBuilder.newMountsListFragmentInstance(PageFragment.MountTypes.values()[position])

    override fun getPageTitle(position: Int): String = WowMountApp.getContext().resources.getStringArray(R.array.mounts_groups)[position]
}