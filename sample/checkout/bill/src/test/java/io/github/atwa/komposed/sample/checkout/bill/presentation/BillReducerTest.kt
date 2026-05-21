package io.github.atwa.komposed.sample.checkout.bill.presentation

import io.github.atwa.komposed.sample.checkout.bill.domain.BillSummary
import io.github.atwa.komposed.testing.assertEffect
import io.github.atwa.komposed.testing.assertNoEffect
import io.github.atwa.komposed.testing.assertState
import io.github.atwa.komposed.testing.given
import org.junit.Test

class BillReducerTest {

    @Test
    fun `FetchBillSummary sets isLoading and emits FetchSummary effect`() {
        billReducer.given(BillState(), BillAction.FetchBillSummary("user123"))
            .assertState(BillState(isLoading = true))
            .assertEffect<BillEffect.FetchSummary> { effect ->
                assert(effect.userId == "user123")
            }
    }

    @Test
    fun `BillSummaryLoaded updates fees and clears isLoading`() {
        val summary = BillSummary(serviceFees = 5.0, orderTotal = 100.0)
        billReducer.given(BillState(isLoading = true), BillAction.BillSummaryLoaded(summary))
            .assertState(BillState(serviceFees = 5.0, orderTotal = 100.0, isLoading = false))
            .assertNoEffect()
    }

    @Test
    fun `BillSummaryFailed clears isLoading with no effect`() {
        billReducer.given(BillState(isLoading = true), BillAction.BillSummaryFailed("timeout"))
            .assertState(BillState(isLoading = false))
            .assertNoEffect()
    }

    @Test
    fun `DeliveryFeeUpdated stores the new fee with no effect`() {
        billReducer.given(BillState(), BillAction.DeliveryFeeUpdated(12.5))
            .assertState(BillState(deliveryFees = 12.5))
            .assertNoEffect()
    }

    @Test
    fun `DeliveryFeeUpdated overwrites a previously set delivery fee`() {
        billReducer.given(BillState(deliveryFees = 5.0), BillAction.DeliveryFeeUpdated(18.0))
            .assertState(BillState(deliveryFees = 18.0))
            .assertNoEffect()
    }
}
