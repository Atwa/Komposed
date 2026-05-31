package io.github.atwa.komposed.app.checkout.placeorder.data

import io.github.atwa.komposed.app.checkout.placeorder.presentation.CheckoutParams
import io.github.atwa.komposed.app.checkout.placeorder.presentation.PlaceOrderAction
import io.github.atwa.komposed.app.checkout.placeorder.presentation.PlaceOrderEffect
import io.github.atwa.komposed.testing.handle
import kotlinx.coroutines.test.runTest
import org.junit.Test

class PlaceOrderEffectHandlerImplTest {

    private fun handlerWith(repository: CheckoutRepository) = PlaceOrderEffectHandlerImpl(repository)

    private val params = CheckoutParams(
        addressId = 1L,
        deliveryNote = "Ring bell",
        addressLine = "123 Main St",
        city = "Cairo",
        deliveryFees = 10.0,
        serviceFees = 5.0,
        orderTotal = 100.0,
    )

    private val successRepo = object : CheckoutRepository {
        override suspend fun placeOrder(request: CheckoutRequest) = Result.success(Unit)
    }

    @Test
    fun `PlaceOrder dispatches OrderPlaced with ORD-prefixed id on success`() = runTest {
        val result = handlerWith(successRepo).handle(PlaceOrderEffect.PlaceOrder(params))
        assert(result.single() is PlaceOrderAction.OrderPlaced)
        assert((result.single() as PlaceOrderAction.OrderPlaced).orderId.startsWith("ORD-"))
    }

    @Test
    fun `PlaceOrder maps all params fields into OrderPlaced on success`() = runTest {
        val customParams = params.copy(
            deliveryNote = "Leave at door",
            addressLine = "456 Pyramid Ave",
            city = "Giza",
            deliveryFees = 7.5,
            serviceFees = 5.0,
            orderTotal = 80.0,
        )
        val action = handlerWith(successRepo).handle(PlaceOrderEffect.PlaceOrder(customParams)).single()
                as PlaceOrderAction.OrderPlaced
        assert(action.addressLine == "456 Pyramid Ave")
        assert(action.city == "Giza")
        assert(action.deliveryNote == "Leave at door")
        assert(action.deliveryFee == 7.5)
        assert(action.serviceFees == 5.0)
        assert(action.orderTotal == 80.0)
    }

    @Test
    fun `PlaceOrder dispatches CheckoutFailed with message on failure`() = runTest {
        val repo = object : CheckoutRepository {
            override suspend fun placeOrder(request: CheckoutRequest) =
                Result.failure<Unit>(RuntimeException("Payment declined"))
        }
        val result = handlerWith(repo).handle(PlaceOrderEffect.PlaceOrder(params))
        assert(result.single() == PlaceOrderAction.CheckoutFailed("Payment declined"))
    }

    @Test
    fun `PlaceOrder dispatches CheckoutFailed with fallback when message is null`() = runTest {
        val repo = object : CheckoutRepository {
            override suspend fun placeOrder(request: CheckoutRequest) =
                Result.failure<Unit>(RuntimeException())
        }
        val result = handlerWith(repo).handle(PlaceOrderEffect.PlaceOrder(params))
        assert(result.single() == PlaceOrderAction.CheckoutFailed("Unknown error"))
    }
}
