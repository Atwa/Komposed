package io.github.atwa.komposed.sample.checkout.bill.data

import io.github.atwa.komposed.sample.checkout.bill.presentation.BillAction
import io.github.atwa.komposed.sample.checkout.bill.presentation.BillEffectHandler
import javax.inject.Inject

class BillEffectHandlerImpl @Inject constructor(
    private val repository: BillSummaryRepository,
) : BillEffectHandler {

    override suspend fun fetchBillSummary(userId: String): BillAction =
        repository.getBillSummary().fold(
            onSuccess = { BillAction.BillSummaryLoaded(it) },
            onFailure = { BillAction.BillSummaryFailed(it.message ?: "Unknown error") },
        )
}
