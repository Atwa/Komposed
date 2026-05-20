package io.github.atwa.komposed.sample.checkout.delivery.data

import io.github.atwa.komposed.sample.checkout.delivery.domain.DeliveryAddress
import io.github.atwa.komposed.sample.checkout.delivery.presentation.DeliveryAction
import io.github.atwa.komposed.sample.checkout.delivery.presentation.DeliveryEffect
import io.github.atwa.komposed.testing.handle
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DeliveryEffectHandlerImplTest {

    private val address = DeliveryAddress(id = 1L, addressLine = "123 Nile St", city = "Cairo", deliveryFee = 5.0)

    private fun handlerWith(repository: DeliveryRepository) = DeliveryEffectHandlerImpl(repository)

    @Test
    fun `FetchAddresses dispatches OnDeliveryAddressLoaded on success`() = runTest {
        val repo = object : DeliveryRepository {
            override suspend fun fetchDeliveryAddresses() = Result.success(listOf(address))
        }
        val result = handlerWith(repo).handle(DeliveryEffect.FetchAddresses)
        assert(result.single() == DeliveryAction.OnDeliveryAddressLoaded(listOf(address)))
    }

    @Test
    fun `FetchAddresses dispatches OnDeliveryAddressFailure with message on failure`() = runTest {
        val repo = object : DeliveryRepository {
            override suspend fun fetchDeliveryAddresses() =
                Result.failure<List<DeliveryAddress>>(RuntimeException("Network error"))
        }
        val result = handlerWith(repo).handle(DeliveryEffect.FetchAddresses)
        assert(result.single() == DeliveryAction.OnDeliveryAddressFailure("Network error"))
    }

    @Test
    fun `FetchAddresses dispatches OnDeliveryAddressFailure with fallback when message is null`() = runTest {
        val repo = object : DeliveryRepository {
            override suspend fun fetchDeliveryAddresses() =
                Result.failure<List<DeliveryAddress>>(RuntimeException())
        }
        val result = handlerWith(repo).handle(DeliveryEffect.FetchAddresses)
        assert(result.single() == DeliveryAction.OnDeliveryAddressFailure("Unknown error"))
    }
}
