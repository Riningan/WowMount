package com.riningan.wowmount.ui

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.riningan.util.Logger
import com.riningan.wowmount.R
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
    private var mNavigator: Navigator? = null


    override fun attachBaseContext(newBase: Context) {
        Logger.debug()
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Logger.debug()
        super.onCreate(null)
        setContentView(R.layout.activity_main)
        mNavigator = Navigator(this, android.R.id.content)
        mRouter.newRootScreen(SplashFragment::class.java.canonicalName)
    }

    override fun onStart() {
        Logger.debug()
        super.onStart()
    }

    override fun onResume() {
        Logger.debug()
        super.onResume()
        mNavigatorHolder.setNavigator(mNavigator)
    }

    override fun onPause() {
        Logger.debug()
        mNavigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onStop() {
        Logger.debug()
        super.onStop()
    }

    override fun onDestroy() {
        Logger.debug()
        super.onDestroy()
        mNavigator = null
    }
}