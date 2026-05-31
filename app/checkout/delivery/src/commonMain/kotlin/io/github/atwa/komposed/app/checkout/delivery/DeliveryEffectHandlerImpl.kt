package io.github.atwa.komposed.app.checkout.delivery

import io.github.atwa.komposed.effect.EffectHandler

class DeliveryEffectHandlerImpl(
    private val repository: DeliveryRepository,
) : EffectHandler<DeliveryEffect, DeliveryAction> {

    override suspend fun handle(effect: DeliveryEffect, dispatch: suspend (suspend () -> DeliveryAction) -> Unit) {
        when (effect) {
            DeliveryEffect.FetchAddresses -> dispatch {
                repository.fetchDeliveryAddresses().fold(
                    onSuccess = { DeliveryAction.OnDeliveryAddressLoaded(it) },
                    onFailure = { DeliveryAction.OnDeliveryAddressFailure(it.message ?: "Unknown error") },
                )
            }
        }
    }
}
