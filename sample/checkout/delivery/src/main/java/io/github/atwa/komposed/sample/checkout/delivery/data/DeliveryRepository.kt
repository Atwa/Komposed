package io.github.atwa.komposed.sample.checkout.delivery.data

import io.github.atwa.komposed.sample.checkout.delivery.domain.DeliveryAddress

interface DeliveryRepository {
    suspend fun fetchDeliveryAddresses(): Result<List<DeliveryAddress>>
}
