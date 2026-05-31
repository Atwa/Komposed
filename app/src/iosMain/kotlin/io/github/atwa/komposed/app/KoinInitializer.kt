package io.github.atwa.komposed.app

import io.github.atwa.komposed.app.checkout.checkoutKoinModule
import io.github.atwa.komposed.app.core.navigation.navigationKoinModule
import io.github.atwa.komposed.app.orderdetails.orderDetailsKoinModule
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(
            navigationKoinModule,
            checkoutKoinModule,
            orderDetailsKoinModule,
        )
    }
}
