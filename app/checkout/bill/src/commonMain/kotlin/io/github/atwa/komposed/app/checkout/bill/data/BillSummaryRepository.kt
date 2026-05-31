package io.github.atwa.komposed.app.checkout.bill.data

import io.github.atwa.komposed.app.checkout.bill.domain.BillSummary

interface BillSummaryRepository {
    suspend fun getBillSummary(): Result<BillSummary>
}
