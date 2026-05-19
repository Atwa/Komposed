package io.github.atwa.komposed.sample.checkout.bill.data

import io.github.atwa.komposed.sample.checkout.bill.domain.BillSummary

interface BillSummaryRepository {
    suspend fun getBillSummary(): Result<BillSummary>
}
