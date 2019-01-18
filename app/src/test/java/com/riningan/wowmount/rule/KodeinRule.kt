package com.riningan.wowmount.rule

import com.riningan.wowmount.data.preferences.LocalPreferences
import com.riningan.wowmount.interactor.CharacterInteractor
import io.mockk.mockk
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import ru.terrakok.cicerone.Router


class KodeinRule : TestRule {
    val characterInteractor: CharacterInteractor = mockk()
    val router: Router = mockk(relaxed = true)
    val localPreferences: LocalPreferences = mockk()

    val kodein = Kodein.lazy {
        bind<CharacterInteractor>() with instance(characterInteractor)
        bind<Router>() with instance(router)
        bind<LocalPreferences>() with instance(localPreferences)
    }


    override fun apply(base: Statement, description: Description?) = object : Statement() {
        override fun evaluate() {
            base.evaluate()
        }
    }
}