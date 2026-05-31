package io.github.atwa.komposed.app.checkout.delivery

interface DeliveryRepository {
    suspend fun fetchDeliveryAddresses(): Result<List<DeliveryAddress>>
}
