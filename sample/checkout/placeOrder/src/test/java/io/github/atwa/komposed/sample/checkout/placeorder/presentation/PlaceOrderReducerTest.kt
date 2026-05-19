package io.github.atwa.komposed.sample.checkout.placeorder.presentation

import io.github.atwa.komposed.ActionableEffect
import io.github.atwa.komposed.sample.core.navigation.OrderDetailsRoute
import io.github.atwa.komposed.testing.assertEffect
import io.github.atwa.komposed.testing.assertNavigationEffect
import io.github.atwa.komposed.testing.assertNoEffect
import io.github.atwa.komposed.testing.assertState
import io.github.atwa.komposed.testing.given
import org.junit.Test

class PlaceOrderReducerTest {

    private val fakeHandler = object : PlaceOrderEffectHandler {
        override suspend fun placeOrder(
            addressId: Long,
            deliveryNote: String,
            addressLine: String,
            city: String,
            deliveryFees: Double,
            serviceFees: Double,
            orderTotal: Double,
        ): PlaceOrderAction = PlaceOrderAction.OrderPlaced(
            orderId = "order-1",
            addressLine = addressLine,
            city = city,
            deliveryNote = deliveryNote,
            deliveryFee = deliveryFees,
            serviceFees = serviceFees,
            orderTotal = orderTotal,
        )
    }
    private val reducer = placeOrderReducer.provide(fakeHandler)

    private val validCheckout = PlaceOrderAction.Checkout(
        selectedAddressId = 1L,
        deliveryNote = "Ring bell",
        addressLine = "123 Main St",
        city = "Cairo",
        deliveryFees = 10.0,
        serviceFees = 5.0,
        orderTotal = 100.0,
    )

    @Test
    fun `Checkout with valid address sets in-progress and emits ActionableEffect`() {
        reducer.given(PlaceOrderState(), validCheckout)
            .assertState(PlaceOrderState(isCheckoutInProgress = true, errorMessage = null))
            .assertEffect<ActionableEffect<*>>()
    }

    @Test
    fun `Checkout with null address sets error and does not emit effect`() {
        val noAddress = validCheckout.copy(selectedAddressId = null)
        reducer.given(PlaceOrderState(), noAddress)
            .assertState(PlaceOrderState(isCheckoutInProgress = false, errorMessage = "Please select a delivery address"))
            .assertNoEffect()
    }

    @Test
    fun `OrderPlaced clears in-progress and emits NavigationEffect to OrderDetailsRoute`() {
        val action = PlaceOrderAction.OrderPlaced(
            orderId = "order-1",
            addressLine = "123 Main St",
            city = "Cairo",
            deliveryNote = "Ring bell",
            deliveryFee = 10.0,
            serviceFees = 5.0,
            orderTotal = 100.0,
        )
        reducer.given(PlaceOrderState(isCheckoutInProgress = true), action)
            .assertState(PlaceOrderState(isCheckoutInProgress = false, errorMessage = null))
            .assertNavigationEffect { nav ->
                val route = nav.navigations.first()
                assert(route is OrderDetailsRoute) { "Expected OrderDetailsRoute but got $route" }
                assert((route as OrderDetailsRoute).orderId == "order-1")
            }
    }

    @Test
    fun `CheckoutFailed sets error message and clears in-progress`() {
        reducer.given(PlaceOrderState(isCheckoutInProgress = true), PlaceOrderAction.CheckoutFailed("Server error"))
            .assertState(PlaceOrderState(isCheckoutInProgress = false, errorMessage = "Server error"))
            .assertNoEffect()
    }
}
