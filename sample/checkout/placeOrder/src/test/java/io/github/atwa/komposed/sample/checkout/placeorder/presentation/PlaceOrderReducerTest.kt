package io.github.atwa.komposed.sample.checkout.placeorder.presentation

import io.github.atwa.komposed.sample.core.navigation.OrderDetailsRoute
import io.github.atwa.komposed.testing.assertEffect
import io.github.atwa.komposed.testing.assertNavigationEffect
import io.github.atwa.komposed.testing.assertNoEffect
import io.github.atwa.komposed.testing.assertState
import io.github.atwa.komposed.testing.given
import org.junit.Test

class PlaceOrderReducerTest {

    private val params = CheckoutParams(
        addressId = 1L,
        deliveryNote = "Ring bell",
        addressLine = "123 Main St",
        city = "Cairo",
        deliveryFees = 10.0,
        serviceFees = 5.0,
        orderTotal = 100.0,
    )

    private val paramsNoAddress = CheckoutParams(
        addressId = null,
        deliveryNote = "",
        addressLine = "",
        city = "",
        deliveryFees = 0.0,
        serviceFees = 0.0,
        orderTotal = 0.0,
    )

    @Test
    fun `Checkout with valid addressId sets in-progress and emits PlaceOrder effect`() {
        placeOrderReducer.given(PlaceOrderState(), PlaceOrderAction.Checkout(params))
            .assertState(PlaceOrderState(isCheckoutInProgress = true, errorMessage = null))
            .assertEffect<PlaceOrderEffect.PlaceOrder> { effect ->
                assert(effect.params == params)
            }
    }

    @Test
    fun `Checkout with null addressId sets error and does not emit effect`() {
        placeOrderReducer.given(PlaceOrderState(), PlaceOrderAction.Checkout(paramsNoAddress))
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
        placeOrderReducer.given(PlaceOrderState(isCheckoutInProgress = true), action)
            .assertState(PlaceOrderState(isCheckoutInProgress = false, errorMessage = null))
            .assertNavigationEffect { nav ->
                val route = nav.navigations.first()
                assert(route is OrderDetailsRoute) { "Expected OrderDetailsRoute but got $route" }
                assert((route as OrderDetailsRoute).orderId == "order-1")
            }
    }

    @Test
    fun `CheckoutFailed sets error message and clears in-progress`() {
        placeOrderReducer.given(PlaceOrderState(isCheckoutInProgress = true), PlaceOrderAction.CheckoutFailed("Server error"))
            .assertState(PlaceOrderState(isCheckoutInProgress = false, errorMessage = "Server error"))
            .assertNoEffect()
    }
}
