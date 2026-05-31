package io.github.atwa.komposed.app.orderdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.atwa.komposed.app.checkout.bill.BillState
import io.github.atwa.komposed.app.core.navigation.OrderDetailsRoute
import io.github.atwa.komposed.app.orderdetails.OrderDetailsState.Companion.billLens
import io.github.atwa.komposed.app.orderdetails.OrderDetailsState.Companion.orderInfoLens
import io.github.atwa.komposed.app.orderdetails.bill.orderDetailsBillReducer
import io.github.atwa.komposed.createStore
import io.github.atwa.komposed.reducer.reducers

class OrderDetailsViewModel(route: OrderDetailsRoute) : ViewModel() {

    val store by lazy {
        createStore(
            initialValue = OrderDetailsState(
                orderInfoState = OrderInfoState(
                    orderId = route.orderId,
                    totalItemsCount = route.totalItemsCount,
                    addressLine = route.addressLine,
                    city = route.city,
                    deliveryNote = route.deliveryNote,
                ),
                billState = BillState(
                    serviceFees = route.serviceFees,
                    orderTotal = route.orderTotal,
                ),
                deliveryFee = route.deliveryFee,
            ),
            scope = viewModelScope,
            reducers = reducers {
                orderInfoReducer.scoped(orderInfoLens)
                orderDetailsBillReducer.scoped(billLens)
            },
        )
    }
}
