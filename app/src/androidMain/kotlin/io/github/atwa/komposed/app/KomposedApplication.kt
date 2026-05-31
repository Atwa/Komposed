package io.github.atwa.komposed.app

import android.app.Application
import io.github.atwa.komposed.app.checkout.checkoutKoinModule
import io.github.atwa.komposed.app.core.navigation.navigationKoinModule
import io.github.atwa.komposed.app.orderdetails.orderDetailsKoinModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KomposedApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@KomposedApplication)
            modules(
                navigationKoinModule,
                checkoutKoinModule,
                orderDetailsKoinModule,
            )
        }
    }
}
