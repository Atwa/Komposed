package io.github.atwa.komposed.app.checkout.delivery

import io.github.atwa.komposed.effect.EffectHandler
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val deliveryKoinModule = module {
    single<DeliveryRepository> { DeliveryRepositoryImpl() }
    single<EffectHandler<DeliveryEffect, DeliveryAction>>(qualifier<DeliveryEffectHandlerImpl>()) { DeliveryEffectHandlerImpl(get()) }
}
