package io.github.atwa.komposed.app.checkout.placeorder

data class CheckoutSummary(
    val selectedAddressId: Long? = null,
    val deliveryNote: String = "",
    val serviceFees: Double = 0.0,
    val orderTotal: Double = 0.0,
    val deliveryFees: Double = 0.0
)