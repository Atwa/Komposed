package io.github.atwa.komposed.sample.orderdetails.bill

import io.github.atwa.komposed.sample.checkout.bill.presentation.BillState
import io.github.atwa.komposed.reducer.ReduceType.Companion.reduce
import io.github.atwa.komposed.reducer.reducer

interface OrderDetailsBillAction

val orderDetailsBillReducer = reducer<BillState, OrderDetailsBillAction> { state, _ ->
    state.reduce()
}
