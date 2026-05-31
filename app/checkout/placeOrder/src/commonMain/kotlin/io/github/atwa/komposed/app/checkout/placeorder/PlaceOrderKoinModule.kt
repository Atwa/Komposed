package io.github.atwa.komposed.app.checkout.placeorder

import io.github.atwa.komposed.effect.EffectHandler
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val placeOrderKoinModule = module {
    single<CheckoutRepository> { CheckoutRepositoryImpl() }
    single<EffectHandler<PlaceOrderEffect, PlaceOrderAction>>(qualifier<PlaceOrderEffectHandlerImpl>()) { PlaceOrderEffectHandlerImpl(get()) }
}
