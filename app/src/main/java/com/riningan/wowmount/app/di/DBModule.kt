package com.riningan.wowmount.app.di

import com.riningan.wowmount.data.db.DBHelper
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


fun getDBModule() = Kodein.Module(name = "DB") {
    bind<DBHelper>() with singleton { DBHelper(instance()) }
}