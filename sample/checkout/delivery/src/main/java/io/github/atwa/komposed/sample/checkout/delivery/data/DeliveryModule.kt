package io.github.atwa.komposed.sample.checkout.delivery.data

import io.github.atwa.komposed.sample.checkout.delivery.presentation.DeliveryEffectHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DeliveryModule {
    @Binds
    abstract fun bindDeliveryEffectHandler(impl: DeliveryEffectHandlerImpl): DeliveryEffectHandler

    @Binds
    abstract fun bindDeliveryRepository(impl: DeliveryRepositoryImpl): DeliveryRepository
}
