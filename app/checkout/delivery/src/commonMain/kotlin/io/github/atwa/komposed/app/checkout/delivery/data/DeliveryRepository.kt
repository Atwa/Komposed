package io.github.atwa.komposed.app.checkout.delivery.data

import io.github.atwa.komposed.app.checkout.delivery.domain.DeliveryAddress

interface DeliveryRepository {
    suspend fun fetchDeliveryAddresses(): Result<List<DeliveryAddress>>
}
