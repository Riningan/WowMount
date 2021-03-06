package com.riningan.wowmount.presentation.route

import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.ViewCompat
import com.riningan.frarg.processor.*
import com.riningan.wowmount.presentation.ui.about.AboutFragment
import com.riningan.wowmount.presentation.ui.authorization.AuthorizationFragment
import com.riningan.wowmount.presentation.ui.filter.FilterFragment
import com.riningan.wowmount.presentation.ui.mount.MountFragment
import com.riningan.wowmount.presentation.ui.mounts.MountsFragment
import com.riningan.wowmount.presentation.ui.splash.SplashFragment
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward


class Navigator constructor(activity: FragmentActivity, containerId: Int) : SupportAppNavigator(activity, containerId) {
    override fun createFragment(screenKey: String, data: Any?): Fragment? {
        return when (screenKey) {
            SplashFragment::class.java.canonicalName -> FragmentBuilder.newSplashFragmentInstance(if (data == null) SplashFragmentArgs() else data as SplashFragmentArgs)
            AuthorizationFragment::class.java.canonicalName -> FragmentBuilder.newAuthorizationFragmentInstance()
            MountsFragment::class.java.canonicalName -> FragmentBuilder.newMountsFragmentInstance(data as MountsFragmentArgs)
            MountFragment::class.java.canonicalName -> FragmentBuilder.newMountFragmentInstance(data as MountFragmentArgs)
            AboutFragment::class.java.canonicalName -> FragmentBuilder.newAboutFragmentInstance()
            FilterFragment::class.java.canonicalName -> FragmentBuilder.newFilterFragmentInstance(data as FilterFragmentArgs)
            else -> null
        }
    }

    override fun createActivityIntent(context: Context, screenKey: String, data: Any?): Intent? {
        return null
    }

    override fun setupFragmentTransactionAnimation(command: Command?, currentFragment: Fragment?, nextFragment: Fragment?, fragmentTransaction: FragmentTransaction) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (command is Forward && currentFragment is SplashFragment && nextFragment is AuthorizationFragment) {
                currentFragment.getLogoForAnimation().let {
                    fragmentTransaction.addSharedElement(it, ViewCompat.getTransitionName(it)!!)
                }
            } else if (command is Forward && currentFragment is MountsFragment && nextFragment is MountFragment) {
                currentFragment.getIconForAnimation()?.let {
                    fragmentTransaction.addSharedElement(it, ViewCompat.getTransitionName(it)!!)
                }
            }
        }
    }
}