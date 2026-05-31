package io.github.atwa.komposed.app.checkout.delivery.data

import io.github.atwa.komposed.app.checkout.delivery.domain.DeliveryAddress
import kotlinx.coroutines.delay

class DeliveryRepositoryImpl : DeliveryRepository {
    override suspend fun fetchDeliveryAddresses(): Result<List<DeliveryAddress>> {
        delay(2000)
        return Result.success(
            listOf(
                DeliveryAddress(1, "123 Nile St", "Cairo", deliveryFee = 5.00, isDefault = true),
                DeliveryAddress(2, "456 Pyramid Ave", "Giza", deliveryFee = 7.50),
                DeliveryAddress(3, "789 Alexandria Rd", "Alexandria", deliveryFee = 20.00),
            )
        )
    }
}
