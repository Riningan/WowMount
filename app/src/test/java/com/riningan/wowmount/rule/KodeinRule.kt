package com.riningan.wowmount.rule

import com.riningan.wowmount.data.preferences.LocalPreferences
import com.riningan.wowmount.interactor.CharacterInteractor
import com.riningan.wowmount.utils.SchedulersProvider
import io.mockk.every
import io.mockk.mockk
import io.reactivex.schedulers.TestScheduler
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import ru.terrakok.cicerone.Router


class KodeinRule : TestRule {
    val characterInteractor: CharacterInteractor = mockk()
    val router: Router = mockk(relaxed = true)
    val localPreferences: LocalPreferences = mockk()
    val schedulersProvider: SchedulersProvider = mockk()

    val kodein = Kodein.lazy {
        bind<CharacterInteractor>() with instance(characterInteractor)
        bind<Router>() with instance(router)
        bind<LocalPreferences>() with instance(localPreferences)
        bind<SchedulersProvider>() with provider { schedulersProvider }
    }


    override fun apply(base: Statement, description: Description?) = object : Statement() {
        override fun evaluate() {
            val testScheduler = TestScheduler()
            every { schedulersProvider.executorThread() } returns testScheduler
            every { schedulersProvider.postExecutionThread() } returns testScheduler
            base.evaluate()
        }
    }
}