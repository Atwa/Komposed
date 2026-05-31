package io.github.atwa.komposed.app.orderdetails

import io.github.atwa.komposed.app.checkout.bill.presentation.BillState
import io.github.atwa.komposed.app.orderdetails.orderinfo.OrderInfoState
import io.github.atwa.komposed.lens

data class OrderDetailsState(
    val orderInfoState: OrderInfoState,
    val billState: BillState,
    val deliveryFee: Double,
) {
    val orderGrandTotal: Double get() = billState.orderTotal + billState.serviceFees + deliveryFee

    companion object {
        val orderInfoLens = lens(OrderDetailsState::orderInfoState) { copy(orderInfoState = it) }
        val billLens = lens(OrderDetailsState::billState) { copy(billState = it) }
    }
}
