package io.github.atwa.komposed.app.checkout.delivery

data class DeliveryAddress(
    val id: Long,
    val addressLine: String,
    val city: String,
    val isDefault: Boolean = false,
    val deliveryFee: Double = 0.0
)
