package com.riningan.wowmount.ui

import android.content.Context
import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.riningan.util.Logger
import com.riningan.wowmount.BuildConfig
import com.riningan.wowmount.R
import com.riningan.wowmount.app.WowMountApp
import com.riningan.wowmount.route.Navigator
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import ru.terrakok.cicerone.NavigatorHolder


class MainActivity : MvpAppCompatActivity(), KodeinAware, MainView {
    override val kodein by closestKodein()

    @InjectPresenter
    lateinit var mPresenter: MainPresenter

    private val mNavigatorHolder: NavigatorHolder by instance()
    private var mNavigator: Navigator? = null


    @ProvidePresenter
    fun providePresenter() = MainPresenter(kodein)

    override fun attachBaseContext(newBase: Context) {
        Logger.debug()
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Logger.debug()
        super.onCreate(null)
        setContentView(R.layout.activity_main)
        mNavigator = Navigator(this, android.R.id.content)
        mPresenter.startNavigation()
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
        // LeakCanary
        if (BuildConfig.DEBUG) {
            WowMountApp.getRefWatcher().watch(this)
        }
    }
}