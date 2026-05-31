package io.github.atwa.komposed.app.checkout.bill

interface BillSummaryRepository {
    suspend fun getBillSummary(): Result<BillSummary>
}
