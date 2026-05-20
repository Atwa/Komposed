package io.github.atwa.komposed.sample.checkout.delivery.data

import io.github.atwa.komposed.effect.EffectHandler
import io.github.atwa.komposed.sample.checkout.delivery.presentation.DeliveryAction
import io.github.atwa.komposed.sample.checkout.delivery.presentation.DeliveryEffect
import javax.inject.Inject

class DeliveryEffectHandlerImpl @Inject constructor(
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
