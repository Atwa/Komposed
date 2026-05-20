package io.github.atwa.komposed.sample.checkout.bill.data

import io.github.atwa.komposed.effect.EffectHandler
import io.github.atwa.komposed.sample.checkout.bill.presentation.BillAction
import io.github.atwa.komposed.sample.checkout.bill.presentation.BillEffect
import javax.inject.Inject

class BillEffectHandlerImpl @Inject constructor(
    private val repository: BillSummaryRepository,
) : EffectHandler<BillEffect, BillAction> {

    override suspend fun handle(effect: BillEffect, dispatch: suspend (suspend () -> BillAction) -> Unit) {
        when (effect) {
            is BillEffect.FetchSummary -> dispatch {
                repository.getBillSummary().fold(
                    onSuccess = { BillAction.BillSummaryLoaded(it) },
                    onFailure = { BillAction.BillSummaryFailed(it.message ?: "Unknown error") },
                )
            }
        }
    }
}
