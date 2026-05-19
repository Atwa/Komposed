package io.github.atwa.komposed.sample.checkout.placeorder.data

import io.github.atwa.komposed.sample.checkout.placeorder.presentation.PlaceOrderEffectHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PlaceOrderModule {
    @Binds
    abstract fun bindPlaceOrderEffectHandler(impl: PlaceOrderEffectHandlerImpl): PlaceOrderEffectHandler

    @Binds
    abstract fun bindCheckoutRepository(impl: CheckoutRepositoryImpl): CheckoutRepository
}
