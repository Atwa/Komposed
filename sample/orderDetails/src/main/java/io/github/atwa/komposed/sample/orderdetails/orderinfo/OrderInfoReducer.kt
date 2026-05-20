package io.github.atwa.komposed.sample.orderdetails.orderinfo

import io.github.atwa.komposed.reducer.ReduceType.Companion.reduce
import io.github.atwa.komposed.reducer.pureReducer

data class OrderInfoState(
    val orderId: String,
    val totalItemsCount: Int,
    val addressLine: String,
    val city: String,
    val deliveryNote: String,
)

interface OrderInfoAction

val orderInfoReducer = pureReducer<OrderInfoState, OrderInfoAction> { state, _ ->
    state.reduce()
}
