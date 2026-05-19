package io.github.atwa.komposed.sample.checkout.delivery.presentation

interface DeliveryEffectHandler {
    suspend fun fetchDeliveryAddresses(): DeliveryAction
}
