package io.github.atwa.komposed.app.orderdetails

import io.github.atwa.komposed.app.orderdetails.orderinfo.OrderInfoAction
import io.github.atwa.komposed.app.orderdetails.orderinfo.OrderInfoState
import io.github.atwa.komposed.app.orderdetails.orderinfo.orderInfoReducer
import io.github.atwa.komposed.testing.assertNoEffect
import io.github.atwa.komposed.testing.assertNoStateChange
import io.github.atwa.komposed.testing.given
import org.junit.Test

// OrderInfoAction is an empty sealed interface — no actions are defined yet.
// These tests verify the reducer's structural contract: it is a no-op placeholder.
class OrderInfoReducerTest {

    private val state = OrderInfoState(
        orderId = "order-1",
        totalItemsCount = 3,
        addressLine = "123 Main St",
        city = "Cairo",
        deliveryNote = "Ring bell",
    )

    private object StubAction : OrderInfoAction

    @Test
    fun `any action leaves state unchanged with no effect`() {
        orderInfoReducer.given(state, StubAction)
            .assertNoStateChange()
            .assertNoEffect()
    }
}
