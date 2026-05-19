package io.github.atwa.komposed.sample.checkout.placeorder.data

import io.github.atwa.komposed.sample.checkout.placeorder.presentation.PlaceOrderAction
import kotlinx.coroutines.test.runTest
import org.junit.Test

class PlaceOrderEffectHandlerImplTest {

    private fun handlerWith(repository: CheckoutRepository) = PlaceOrderEffectHandlerImpl(repository)

    private val successRepo = object : CheckoutRepository {
        override suspend fun placeOrder(request: CheckoutRequest) = Result.success(Unit)
    }

    @Test
    fun `placeOrder returns OrderPlaced with ORD-prefixed id on success`() = runTest {
        val result = handlerWith(successRepo).placeOrder(
            addressId = 1L,
            deliveryNote = "Ring bell",
            addressLine = "123 Main St",
            city = "Cairo",
            deliveryFees = 10.0,
            serviceFees = 5.0,
            orderTotal = 100.0,
        )
        assert(result is PlaceOrderAction.OrderPlaced)
        assert((result as PlaceOrderAction.OrderPlaced).orderId.startsWith("ORD-"))
    }

    @Test
    fun `placeOrder maps all fields into OrderPlaced on success`() = runTest {
        val result = handlerWith(successRepo).placeOrder(
            addressId = 1L,
            deliveryNote = "Leave at door",
            addressLine = "456 Pyramid Ave",
            city = "Giza",
            deliveryFees = 7.5,
            serviceFees = 5.0,
            orderTotal = 80.0,
        ) as PlaceOrderAction.OrderPlaced

        assert(result.addressLine == "456 Pyramid Ave")
        assert(result.city == "Giza")
        assert(result.deliveryNote == "Leave at door")
        assert(result.deliveryFee == 7.5)
        assert(result.serviceFees == 5.0)
        assert(result.orderTotal == 80.0)
    }

    @Test
    fun `placeOrder returns CheckoutFailed with message on failure`() = runTest {
        val repo = object : CheckoutRepository {
            override suspend fun placeOrder(request: CheckoutRequest) =
                Result.failure<Unit>(RuntimeException("Payment declined"))
        }
        val result = handlerWith(repo).placeOrder(
            addressId = 1L,
            deliveryNote = "",
            addressLine = "",
            city = "",
            deliveryFees = 0.0,
            serviceFees = 0.0,
            orderTotal = 0.0,
        )
        assert(result == PlaceOrderAction.CheckoutFailed("Payment declined"))
    }

    @Test
    fun `placeOrder returns CheckoutFailed with fallback when message is null`() = runTest {
        val repo = object : CheckoutRepository {
            override suspend fun placeOrder(request: CheckoutRequest) =
                Result.failure<Unit>(RuntimeException())
        }
        val result = handlerWith(repo).placeOrder(
            addressId = 1L,
            deliveryNote = "",
            addressLine = "",
            city = "",
            deliveryFees = 0.0,
            serviceFees = 0.0,
            orderTotal = 0.0,
        )
        assert(result == PlaceOrderAction.CheckoutFailed("Unknown error"))
    }
}
