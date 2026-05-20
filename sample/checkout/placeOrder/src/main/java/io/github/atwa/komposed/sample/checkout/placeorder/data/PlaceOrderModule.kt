package io.github.atwa.komposed.sample.checkout.placeorder.data

import io.github.atwa.komposed.effect.EffectHandler
import io.github.atwa.komposed.sample.checkout.placeorder.presentation.PlaceOrderAction
import io.github.atwa.komposed.sample.checkout.placeorder.presentation.PlaceOrderEffect
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PlaceOrderModule {
    @Binds
    abstract fun bindPlaceOrderEffectHandler(impl: PlaceOrderEffectHandlerImpl): EffectHandler<PlaceOrderEffect, PlaceOrderAction>

    @Binds
    abstract fun bindCheckoutRepository(impl: CheckoutRepositoryImpl): CheckoutRepository
}
