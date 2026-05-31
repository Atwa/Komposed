package io.github.atwa.komposed.app.orderdetails.orderinfo

import io.github.atwa.komposed.reducer.ReduceType.Companion.reduce
import io.github.atwa.komposed.reducer.reducer

data class OrderInfoState(
    val orderId: String,
    val totalItemsCount: Int,
    val addressLine: String,
    val city: String,
    val deliveryNote: String,
)

interface OrderInfoAction

val orderInfoReducer = reducer<OrderInfoState, OrderInfoAction> { state, _ ->
    state.reduce()
}
