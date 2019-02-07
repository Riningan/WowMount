package com.riningan.wowmount.test

import android.os.Bundle
import android.support.annotation.RestrictTo
import com.arellomobile.mvp.MvpAppCompatActivity
import com.riningan.wowmount.R
import com.riningan.wowmount.presentation.route.Navigator
import com.riningan.wowmount.presentation.ui.authorization.AuthorizationFragment
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        setContentView(R.layout.activity_main)
        mNavigator = Navigator(this, android.R.id.content)
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


    fun setFragment(fragmentClass: Class<out BaseFragment>) {
        runOnUiThread {
            mRouter.newRootScreen(fragmentClass.canonicalName)
        }
    }
}