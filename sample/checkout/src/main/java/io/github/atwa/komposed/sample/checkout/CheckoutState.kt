package io.github.atwa.komposed.sample.checkout

import io.github.atwa.komposed.lens
import io.github.atwa.komposed.sample.checkout.bill.presentation.BillState
import io.github.atwa.komposed.sample.checkout.delivery.presentation.DeliveryState
import io.github.atwa.komposed.sample.checkout.placeorder.presentation.CheckoutParams
import io.github.atwa.komposed.sample.checkout.placeorder.presentation.PlaceOrderState

data class CheckoutState(
    val deliveryState: DeliveryState = DeliveryState(),
    val billState: BillState = BillState(),
    val placeOrderState: PlaceOrderState = PlaceOrderState(),
) {
    val isLoading: Boolean get() = deliveryState.isLoading || billState.isLoading
    val deliveryFee: Double get() = deliveryState.selectedAddress?.deliveryFee ?: 0.0
    val error: String? get() = deliveryState.error
    val orderGrandTotal: Double get() = billState.orderTotal + billState.serviceFees + deliveryFee

    fun toCheckoutParams() = CheckoutParams(
        addressId = deliveryState.selectedAddressId,
        deliveryNote = deliveryState.deliveryNote,
        addressLine = deliveryState.selectedAddress?.addressLine ?: "",
        city = deliveryState.selectedAddress?.city ?: "",
        deliveryFees = deliveryFee,
        serviceFees = billState.serviceFees,
        orderTotal = billState.orderTotal,
    )

    companion object {
        val deliveryLens = lens(CheckoutState::deliveryState) { copy(deliveryState = it) }
        val billLens = lens(CheckoutState::billState) { copy(billState = it) }
        val placeOrderLens = lens(CheckoutState::placeOrderState) { copy(placeOrderState = it) }
    }
}
