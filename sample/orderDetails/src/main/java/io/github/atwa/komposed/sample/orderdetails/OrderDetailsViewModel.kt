package io.github.atwa.komposed.sample.orderdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import io.github.atwa.komposed.sample.checkout.bill.presentation.BillState
import io.github.atwa.komposed.createStore
import io.github.atwa.komposed.reducers
import io.github.atwa.komposed.sample.core.navigation.OrderDetailsRoute
import io.github.atwa.komposed.sample.orderdetails.OrderDetailsState.Companion.billLens
import io.github.atwa.komposed.sample.orderdetails.OrderDetailsState.Companion.orderInfoLens
import io.github.atwa.komposed.sample.orderdetails.bill.orderDetailsBillReducer
import io.github.atwa.komposed.sample.orderdetails.orderinfo.OrderInfoState
import io.github.atwa.komposed.sample.orderdetails.orderinfo.orderInfoReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val route: OrderDetailsRoute = savedStateHandle.toRoute()

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
            scope = viewModelScope + Dispatchers.Main.immediate,
            reducers = reducers {
                orderInfoReducer.scoped(orderInfoLens)
                orderDetailsBillReducer.scoped(billLens)
            },
        )
    }
}
