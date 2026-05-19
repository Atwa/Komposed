package io.github.atwa.komposed.sample.orderdetails.bill

import io.github.atwa.komposed.sample.checkout.bill.presentation.BillState
import io.github.atwa.komposed.ReduceType.Companion.reduce
import io.github.atwa.komposed.pureReducer

interface OrderDetailsBillAction

val orderDetailsBillReducer = pureReducer<BillState, OrderDetailsBillAction> { state, _ ->
    state.reduce()
}
