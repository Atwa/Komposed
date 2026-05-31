package io.github.atwa.komposed.app.orderdetails

import io.github.atwa.komposed.app.checkout.bill.presentation.BillState
import io.github.atwa.komposed.app.orderdetails.bill.OrderDetailsBillAction
import io.github.atwa.komposed.app.orderdetails.bill.orderDetailsBillReducer
import io.github.atwa.komposed.testing.assertNoEffect
import io.github.atwa.komposed.testing.assertNoStateChange
import io.github.atwa.komposed.testing.given
import org.junit.Test

// OrderDetailsBillAction is an empty sealed interface — no actions are defined yet.
// These tests verify the reducer's structural contract: it is a no-op placeholder.
class OrderDetailsBillReducerTest {

    private val state = BillState(serviceFees = 5.0, orderTotal = 100.0)

    // Kotlin does not allow instantiating an empty sealed interface directly.
    // The test below uses a local subtype to exercise the reducer path.
    private object StubAction : OrderDetailsBillAction

    @Test
    fun `any action leaves state unchanged with no effect`() {
        orderDetailsBillReducer.given(state, StubAction)
            .assertNoStateChange()
            .assertNoEffect()
    }
}
