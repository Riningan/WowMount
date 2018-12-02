package com.riningan.wowmount.di

import com.riningan.wowmount.ui.mounts.MountsFragment
import com.riningan.wowmount.ui.mounts.MountsPresenter
import org.kodein.di.Kodein
import org.kodein.di.android.support.AndroidLifecycleScope
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.scoped
import org.kodein.di.generic.singleton


val presentersModule = Kodein.Module(name = "Presenters") {
    bind<MountsPresenter>() with scoped(AndroidLifecycleScope<MountsFragment>()).singleton { MountsPresenter(instance()) }
}