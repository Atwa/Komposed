package io.github.atwa.komposed.sample.checkout.delivery.data

import io.github.atwa.komposed.sample.checkout.delivery.presentation.DeliveryAction
import io.github.atwa.komposed.sample.checkout.delivery.presentation.DeliveryEffectHandler
import javax.inject.Inject

class DeliveryEffectHandlerImpl @Inject constructor(
    private val repository: DeliveryRepository,
) : DeliveryEffectHandler {

    override suspend fun fetchDeliveryAddresses(): DeliveryAction =
        repository.fetchDeliveryAddresses().fold(
            onSuccess = { DeliveryAction.OnDeliveryAddressLoaded(it) },
            onFailure = { DeliveryAction.OnDeliveryAddressFailure(it.message ?: "Unknown error") },
        )
}
