package io.github.atwa.komposed.app.checkout.placeorder.data

data class CheckoutRequest(
    val addressId: Long,
    val deliveryNote: String,
    val serviceFees: Double,
    val orderTotal: Double,
    val deliveryFees: Double
)
