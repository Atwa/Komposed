package io.github.atwa.komposed.app.checkout.bill

import io.github.atwa.komposed.effect.EffectHandler

class BillEffectHandlerImpl(
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
