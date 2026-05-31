package io.github.atwa.komposed.app.checkout.bill.presentation

import io.github.atwa.komposed.effect.Effect

sealed interface BillEffect : Effect {
    data class FetchSummary(val userId: String) : BillEffect
}
