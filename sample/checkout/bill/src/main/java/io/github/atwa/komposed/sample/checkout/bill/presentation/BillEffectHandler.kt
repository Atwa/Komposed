package io.github.atwa.komposed.sample.checkout.bill.presentation

interface BillEffectHandler {
    suspend fun fetchBillSummary(userId: String): BillAction
}
