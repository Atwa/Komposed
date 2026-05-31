package io.github.atwa.komposed.app.checkout.delivery.data

import io.github.atwa.komposed.effect.EffectHandler
import io.github.atwa.komposed.app.checkout.delivery.presentation.DeliveryAction
import io.github.atwa.komposed.app.checkout.delivery.presentation.DeliveryEffect
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val deliveryKoinModule = module {
    single<DeliveryRepository> { DeliveryRepositoryImpl() }
    single<EffectHandler<DeliveryEffect, DeliveryAction>>(qualifier<DeliveryEffectHandlerImpl>()) { DeliveryEffectHandlerImpl(get()) }
}
