package io.github.atwa.komposed.app.checkout

import io.github.atwa.komposed.app.checkout.bill.BillState
import io.github.atwa.komposed.app.checkout.delivery.DeliveryState
import io.github.atwa.komposed.app.checkout.placeorder.CheckoutParams
import io.github.atwa.komposed.app.checkout.placeorder.PlaceOrderState
import io.github.atwa.komposed.lens

data class CheckoutState(
    val deliveryState: DeliveryState = DeliveryState(),
    val billState: BillState = BillState(),
    val placeOrderState: PlaceOrderState = PlaceOrderState(),
) {
    val isLoading: Boolean get() = deliveryState.isLoading || billState.isLoading

    fun toCheckoutParams() = CheckoutParams(
        addressId = deliveryState.selectedAddressId,
        deliveryNote = deliveryState.deliveryNote,
        addressLine = deliveryState.selectedAddress?.addressLine ?: "",
        city = deliveryState.selectedAddress?.city ?: "",
        deliveryFees = billState.deliveryFees,
        serviceFees = billState.serviceFees,
        orderTotal = billState.orderTotal,
    )

    companion object {
        val deliveryLens = lens(CheckoutState::deliveryState) { copy(deliveryState = it) }
        val billLens = lens(CheckoutState::billState) { copy(billState = it) }
        val placeOrderLens = lens(CheckoutState::placeOrderState) { copy(placeOrderState = it) }
    }
}
