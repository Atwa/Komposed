package io.github.atwa.komposed.sample.checkout.placeorder.presentation

import io.github.atwa.komposed.ActionableEffect
import io.github.atwa.komposed.NavigationEffect
import io.github.atwa.komposed.ReduceType.Companion.reduce
import io.github.atwa.komposed.ReduceType.Companion.withEffect
import io.github.atwa.komposed.reducer
import io.github.atwa.komposed.sample.core.navigation.OrderDetailsRoute

data class PlaceOrderState(
    val isCheckoutInProgress: Boolean = false,
    val errorMessage: String? = null,
)

sealed interface PlaceOrderAction {
    data class Checkout(
        val selectedAddressId: Long?,
        val deliveryNote: String,
        val addressLine: String,
        val city: String,
        val deliveryFees: Double,
        val serviceFees: Double,
        val orderTotal: Double,
    ) : PlaceOrderAction

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

val placeOrderReducer =
    reducer<PlaceOrderState, PlaceOrderAction, PlaceOrderEffectHandler> { state, action, handler ->
        when (action) {
            is PlaceOrderAction.Checkout -> action.selectedAddressId?.let { addressId ->
                state.copy(isCheckoutInProgress = true, errorMessage = null).withEffect {
                    ActionableEffect {
                        handler.placeOrder(
                            addressId = addressId,
                            deliveryNote = action.deliveryNote,
                            addressLine = action.addressLine,
                            city = action.city,
                            deliveryFees = action.deliveryFees,
                            serviceFees = action.serviceFees,
                            orderTotal = action.orderTotal,
                        )
                    }
                }
            } ?: state.copy(
                isCheckoutInProgress = false,
                errorMessage = "Please select a delivery address"
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
