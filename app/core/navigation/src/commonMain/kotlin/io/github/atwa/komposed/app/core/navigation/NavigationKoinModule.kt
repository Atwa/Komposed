package io.github.atwa.komposed.app.core.navigation

import io.github.atwa.komposed.navigation.Navigator
import org.koin.dsl.module

val navigationKoinModule = module {
    single { NavigatorImpl() }
    single<Navigator> { get<NavigatorImpl>() }
}
