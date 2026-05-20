package io.github.atwa.komposed.sample.checkout.bill.presentation

import io.github.atwa.komposed.sample.checkout.bill.domain.BillSummary
import io.github.atwa.komposed.reducer.ReduceType.Companion.reduce
import io.github.atwa.komposed.reducer.ReduceType.Companion.withEffect
import io.github.atwa.komposed.reducer.reducer

data class BillState(
    val serviceFees: Double = 0.0,
    val orderTotal: Double = 0.0,
    val isLoading: Boolean = false,
)

sealed interface BillAction {
    data class FetchBillSummary(val userId: String) : BillAction
    data class BillSummaryLoaded(val summary: BillSummary) : BillAction
    data class BillSummaryFailed(val message: String) : BillAction
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
    }
}
