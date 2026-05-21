package io.github.atwa.komposed.sample.checkout.bill.presentation

import io.github.atwa.komposed.sample.checkout.bill.domain.BillSummary
import io.github.atwa.komposed.reducer.ReduceType.Companion.reduce
import io.github.atwa.komposed.reducer.ReduceType.Companion.withEffect
import io.github.atwa.komposed.reducer.reducer

data class BillState(
    val serviceFees: Double = 0.0,
    val orderTotal: Double = 0.0,
    val deliveryFees: Double = 0.0,
    val isLoading: Boolean = false,
)

sealed interface BillAction {
    data class FetchBillSummary(val userId: String) : BillAction
    data class BillSummaryLoaded(val summary: BillSummary) : BillAction
    data class BillSummaryFailed(val message: String) : BillAction
    /** Dispatched by a store subscription when the delivery fee changes in the parent state.
     *  BillModule has no knowledge of DeliveryModule — the wiring lives at the composition level. */
    data class DeliveryFeeUpdated(val fee: Double) : BillAction
}

val billReducer = reducer<BillState, BillAction> { state, action ->
    when (action) {
        is BillAction.FetchBillSummary ->
            state.copy(isLoading = true).withEffect { BillEffect.FetchSummary(action.userId) }

        is BillAction.BillSummaryLoaded -> state.copy(
            serviceFees = action.summary.serviceFees,
            orderTotal = action.summary.orderTotal,
            isLoading = false,
        ).reduce()

        is BillAction.BillSummaryFailed -> state.copy(isLoading = false).reduce()

        is BillAction.DeliveryFeeUpdated -> state.copy(deliveryFees = action.fee).reduce()
    }
}
