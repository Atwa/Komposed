package io.github.atwa.komposed.sample.checkout.delivery.data

import io.github.atwa.komposed.sample.checkout.delivery.domain.DeliveryAddress
import io.github.atwa.komposed.sample.checkout.delivery.presentation.DeliveryAction
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DeliveryEffectHandlerImplTest {

    private val address = DeliveryAddress(id = 1L, addressLine = "123 Nile St", city = "Cairo", deliveryFee = 5.0)

    private fun handlerWith(repository: DeliveryRepository) = DeliveryEffectHandlerImpl(repository)

    @Test
    fun `fetchDeliveryAddresses returns OnDeliveryAddressLoaded on success`() = runTest {
        val repo = object : DeliveryRepository {
            override suspend fun fetchDeliveryAddresses() = Result.success(listOf(address))
        }
        val result = handlerWith(repo).fetchDeliveryAddresses()
        assert(result == DeliveryAction.OnDeliveryAddressLoaded(listOf(address)))
    }

    @Test
    fun `fetchDeliveryAddresses returns OnDeliveryAddressFailure with message on failure`() = runTest {
        val repo = object : DeliveryRepository {
            override suspend fun fetchDeliveryAddresses() =
                Result.failure<List<DeliveryAddress>>(RuntimeException("Network error"))
        }
        val result = handlerWith(repo).fetchDeliveryAddresses()
        assert(result == DeliveryAction.OnDeliveryAddressFailure("Network error"))
    }

    @Test
    fun `fetchDeliveryAddresses returns OnDeliveryAddressFailure with fallback when message is null`() = runTest {
        val repo = object : DeliveryRepository {
            override suspend fun fetchDeliveryAddresses() =
                Result.failure<List<DeliveryAddress>>(RuntimeException())
        }
        val result = handlerWith(repo).fetchDeliveryAddresses()
        assert(result == DeliveryAction.OnDeliveryAddressFailure("Unknown error"))
    }
}
