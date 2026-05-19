package io.github.atwa.komposed.sample.core.navigation

import kotlinx.serialization.Serializable

interface Route

@Serializable
data object CheckoutRoute : Route

@Serializable
data class OrderDetailsRoute(
    val orderId: String,
    val totalItemsCount: Int,
    val addressLine: String,
    val city: String,
    val deliveryNote: String,
    val deliveryFee: Double,
    val serviceFees: Double,
    val orderTotal: Double,
) : Route
