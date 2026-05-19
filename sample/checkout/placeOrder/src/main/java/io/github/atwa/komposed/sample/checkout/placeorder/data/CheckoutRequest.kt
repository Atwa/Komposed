package io.github.atwa.komposed.sample.checkout.placeorder.data

data class CheckoutRequest(
    val addressId: Long,
    val deliveryNote: String,
    val serviceFees: Double,
    val orderTotal: Double,
    val deliveryFees: Double
)
