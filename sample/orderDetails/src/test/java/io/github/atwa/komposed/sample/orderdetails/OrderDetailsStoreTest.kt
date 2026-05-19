package io.github.atwa.komposed.sample.orderdetails

import io.github.atwa.komposed.reducers
import io.github.atwa.komposed.sample.checkout.bill.presentation.BillState
import io.github.atwa.komposed.sample.orderdetails.OrderDetailsState.Companion.billLens
import io.github.atwa.komposed.sample.orderdetails.OrderDetailsState.Companion.orderInfoLens
import io.github.atwa.komposed.sample.orderdetails.bill.orderDetailsBillReducer
import io.github.atwa.komposed.sample.orderdetails.orderinfo.OrderInfoState
import io.github.atwa.komposed.sample.orderdetails.orderinfo.orderInfoReducer
import io.github.atwa.komposed.testing.TestStore
import kotlinx.coroutines.test.runTest
import org.junit.Test

class OrderDetailsStoreTest {

    private val initialState = OrderDetailsState(
        orderInfoState = OrderInfoState(
            orderId = "order-1",
            totalItemsCount = 3,
            addressLine = "123 Main St",
            city = "Cairo",
            deliveryNote = "Ring bell",
        ),
        billState = BillState(serviceFees = 5.0, orderTotal = 100.0),
        deliveryFee = 10.0,
    )

    private fun buildStore(scope: kotlinx.coroutines.test.TestScope) = TestStore(
        initialState = initialState,
        reducers = reducers {
            orderInfoReducer.scoped(orderInfoLens)
            orderDetailsBillReducer.scoped(billLens)
        },
        scope = scope,
    )

    @Test
    fun `initial state matches provided values`() = runTest {
        buildStore(this).assertState(initialState)
    }

    @Test
    fun `orderGrandTotal is computed from billState and deliveryFee`() = runTest {
        assert(initialState.orderGrandTotal == 115.0)
    }

    @Test
    fun `dispatched actions produce no-op state transitions`() = runTest {
        val store = buildStore(this)
        store.assertState(initialState)
        assert(store.dispatchedActions.isEmpty())
    }
}
