package io.github.atwa.komposed.sample.checkout.bill.data

import io.github.atwa.komposed.sample.checkout.bill.domain.BillSummary
import kotlinx.coroutines.delay
import javax.inject.Inject

class BillSummaryRepositoryImpl @Inject constructor() : BillSummaryRepository {
    override suspend fun getBillSummary(): Result<BillSummary> {
        delay(300)
        return Result.success(BillSummary(serviceFees = 5.00, orderTotal = 45.00))
    }
}
