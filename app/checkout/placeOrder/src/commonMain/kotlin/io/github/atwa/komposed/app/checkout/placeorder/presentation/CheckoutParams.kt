package io.github.atwa.komposed.app.checkout.placeorder.presentation

data class CheckoutParams(
    val addressId: Long?,
    val deliveryNote: String,
    val addressLine: String,
    val city: String,
    val deliveryFees: Double,
    val serviceFees: Double,
    val orderTotal: Double,
)
