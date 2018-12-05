package com.riningan.wowmount.route

import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.transition.ChangeBounds
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.ViewCompat
import com.riningan.frarg.processor.FragmentBuilder
import com.riningan.frarg.processor.MountFragmentArgs
import com.riningan.wowmount.ui.authorization.AuthorizationFragment
import com.riningan.wowmount.ui.mount.MountFragment
import com.riningan.wowmount.ui.mounts.MountsFragment
import com.riningan.wowmount.ui.splash.SplashFragment
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward


class Navigator constructor(activity: FragmentActivity, containerId: Int) : SupportAppNavigator(activity, containerId) {

    override fun createFragment(screenKey: String, data: Any?): Fragment? {
        return when (screenKey) {
            SplashFragment::class.java.canonicalName -> FragmentBuilder.newSplashFragmentInstance()
            AuthorizationFragment::class.java.canonicalName -> FragmentBuilder.newAuthorizationFragmentInstance()
            MountsFragment::class.java.canonicalName -> FragmentBuilder.newMountsFragmentInstance()
            MountFragment::class.java.canonicalName -> FragmentBuilder.newMountFragmentInstance(data as String)
            else -> null
        }
    }

    override fun createActivityIntent(context: Context, screenKey: String, data: Any?): Intent? {
        return null
    }

    override fun setupFragmentTransactionAnimation(command: Command?, currentFragment: Fragment?, nextFragment: Fragment?, fragmentTransaction: FragmentTransaction) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (command is Forward && currentFragment is SplashFragment && nextFragment is AuthorizationFragment) {
                val changeBounds = ChangeBounds()
                nextFragment.setSharedElementEnterTransition(changeBounds)
                nextFragment.setSharedElementReturnTransition(changeBounds)
                currentFragment.setSharedElementEnterTransition(changeBounds)
                currentFragment.setSharedElementReturnTransition(changeBounds)
                val view = currentFragment.getLogoForAnimation()
                fragmentTransaction.addSharedElement(view, view.transitionName)
            } else if (command is Forward && currentFragment is MountsFragment && nextFragment is MountFragment) {
//                val changeBounds = ChangeBounds()
//                nextFragment.setSharedElementEnterTransition(changeBounds)
//                nextFragment.setSharedElementReturnTransition(changeBounds)
//                currentFragment.setSharedElementEnterTransition(changeBounds)
//                currentFragment.setSharedElementReturnTransition(changeBounds)
                currentFragment.getIconForAnimation()?.let {
                    fragmentTransaction.addSharedElement(it, ViewCompat.getTransitionName(it))
                }
            }
        }
    }
}