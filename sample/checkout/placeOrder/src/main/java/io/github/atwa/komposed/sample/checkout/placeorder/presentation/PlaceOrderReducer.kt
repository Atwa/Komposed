package io.github.atwa.komposed.sample.checkout.placeorder.presentation

import io.github.atwa.komposed.effect.NavigationEffect
import io.github.atwa.komposed.reducer.ReduceType.Companion.reduce
import io.github.atwa.komposed.reducer.ReduceType.Companion.withEffect
import io.github.atwa.komposed.reducer.pureReducer
import io.github.atwa.komposed.sample.core.navigation.OrderDetailsRoute

data class PlaceOrderState(
    val isCheckoutInProgress: Boolean = false,
    val errorMessage: String? = null,
)

sealed interface PlaceOrderAction {
    data class Checkout(val params: CheckoutParams) : PlaceOrderAction

    data class OrderPlaced(
        val orderId: String,
        val addressLine: String,
        val city: String,
        val deliveryNote: String,
        val deliveryFee: Double,
        val serviceFees: Double,
        val orderTotal: Double,
    ) : PlaceOrderAction

    data class CheckoutFailed(val message: String) : PlaceOrderAction
}

val placeOrderReducer = pureReducer<PlaceOrderState, PlaceOrderAction> { state, action ->
    when (action) {
        is PlaceOrderAction.Checkout -> action.params.addressId?.let {
            state.copy(isCheckoutInProgress = true, errorMessage = null).withEffect {
                PlaceOrderEffect.PlaceOrder(action.params)
            }
        } ?: state.copy(
            isCheckoutInProgress = false,
            errorMessage = "Please select a delivery address",
        ).reduce()

        is PlaceOrderAction.OrderPlaced ->
            state.copy(isCheckoutInProgress = false, errorMessage = null).withEffect {
                NavigationEffect {
                    navigate(
                        OrderDetailsRoute(
                            orderId = action.orderId,
                            totalItemsCount = 3,
                            addressLine = action.addressLine,
                            city = action.city,
                            deliveryNote = action.deliveryNote,
                            deliveryFee = action.deliveryFee,
                            serviceFees = action.serviceFees,
                            orderTotal = action.orderTotal,
                        )
                    )
                }
            }

        is PlaceOrderAction.CheckoutFailed ->
            state.copy(isCheckoutInProgress = false, errorMessage = action.message).reduce()
    }
}
