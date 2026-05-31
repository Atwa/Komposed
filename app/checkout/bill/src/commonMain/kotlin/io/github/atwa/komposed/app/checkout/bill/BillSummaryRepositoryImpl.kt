package io.github.atwa.komposed.app.checkout.bill

import kotlinx.coroutines.delay

class BillSummaryRepositoryImpl : BillSummaryRepository {
    override suspend fun getBillSummary(): Result<BillSummary> {
        delay(300)
        return Result.success(BillSummary(serviceFees = 5.00, orderTotal = 45.00))
    }
}
