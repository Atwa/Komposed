package io.github.atwa.komposed.sample.checkout.placeorder.presentation

interface PlaceOrderEffectHandler {
    suspend fun placeOrder(
        addressId: Long,
        deliveryNote: String,
        addressLine: String,
        city: String,
        deliveryFees: Double,
        serviceFees: Double,
        orderTotal: Double,
    ): PlaceOrderAction
}
