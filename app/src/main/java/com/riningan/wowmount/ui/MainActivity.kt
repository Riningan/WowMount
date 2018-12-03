package com.riningan.wowmount.ui

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.riningan.wowmount.R
import com.riningan.wowmount.data.LocalPreferences
import com.riningan.wowmount.route.Navigator
import com.riningan.wowmount.ui.splash.SplashFragment
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router


class MainActivity : AppCompatActivity(), KodeinAware {
    override val kodein by closestKodein()
    private val mNavigatorHolder: NavigatorHolder by instance()
    private val mRouter: Router by instance()
    private val mLocalPreferences: LocalPreferences by instance()
    private lateinit var mNavigator: Navigator


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
        mLocalPreferences.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        setContentView(R.layout.activity_main)
        mNavigator = Navigator(this, R.id.fl)
        mRouter.newScreenChain(SplashFragment::class.java.canonicalName)
    }

    override fun onResume() {
        super.onResume()
        mNavigatorHolder.setNavigator(mNavigator)
    }

    override fun onPause() {
        mNavigatorHolder.removeNavigator()
        super.onPause()
    }
}
