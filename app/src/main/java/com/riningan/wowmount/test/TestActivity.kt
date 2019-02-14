package com.riningan.wowmount.test

import android.os.Bundle
import android.support.annotation.RestrictTo
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import android.view.WindowManager
import com.arellomobile.mvp.MvpAppCompatActivity
import com.riningan.wowmount.R
import com.riningan.wowmount.presentation.route.Navigator
import com.riningan.wowmount.presentation.ui.base.BaseFragment
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router


@RestrictTo(RestrictTo.Scope.TESTS)
class TestActivity : MvpAppCompatActivity(), KodeinAware {
    override val kodein by closestKodein()

    private val mNavigatorHolder: NavigatorHolder by instance()
    private val mRouter: Router by instance()
    private var mNavigator: Navigator? = null


    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        setContentView(R.layout.activity_main)

        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON)

        mNavigator = Navigator(this, android.R.id.content)

        supportFragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
                super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            }
        }, true)
    }

    override fun onResume() {
        super.onResume()
        mNavigatorHolder.setNavigator(mNavigator)
    }

    override fun onPause() {
        mNavigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mNavigator = null
    }


    fun setFragment(fragmentClass: Class<out BaseFragment>, data: Any? = null) {
        runOnUiThread {
            mRouter.newRootScreen(fragmentClass.canonicalName, data)
        }
    }
}