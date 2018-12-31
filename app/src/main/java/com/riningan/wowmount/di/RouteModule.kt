package com.riningan.wowmount.di

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router


val routeModule = Kodein.Module(name = "Route") {
    bind<Router>() with singleton { Router() }
    bind<Cicerone<Router>>() with singleton { Cicerone.create<Router>(instance()) }
    bind<NavigatorHolder>() with singleton { instance<Cicerone<Router>>().navigatorHolder }
}