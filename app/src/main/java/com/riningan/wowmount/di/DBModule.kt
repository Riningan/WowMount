package com.riningan.wowmount.di

import com.riningan.wowmount.data.db.CharacterDBHelper
import com.riningan.wowmount.data.db.DBHelper
import io.realm.Realm
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


val dbModule = Kodein.Module(name = "DB") {
    bind<Realm>() with singleton { Realm.getDefaultInstance() }
    bind<DBHelper>() with singleton { DBHelper() }
    bind<CharacterDBHelper>() with singleton { instance<DBHelper>() }
}