package io.github.atwa.komposed.sample.checkout.bill.data

import io.github.atwa.komposed.sample.checkout.bill.domain.BillSummary
import io.github.atwa.komposed.sample.checkout.bill.presentation.BillAction
import kotlinx.coroutines.test.runTest
import org.junit.Test

class BillEffectHandlerImplTest {

    private fun handlerWith(repository: BillSummaryRepository) = BillEffectHandlerImpl(repository)

    @Test
    fun `fetchBillSummary returns BillSummaryLoaded on success`() = runTest {
        val summary = BillSummary(serviceFees = 5.0, orderTotal = 100.0)
        val repo = object : BillSummaryRepository {
            override suspend fun getBillSummary() = Result.success(summary)
        }
        val result = handlerWith(repo).fetchBillSummary("user123")
        assert(result == BillAction.BillSummaryLoaded(summary))
    }

    @Test
    fun `fetchBillSummary returns BillSummaryFailed with message on failure`() = runTest {
        val repo = object : BillSummaryRepository {
            override suspend fun getBillSummary() =
                Result.failure<BillSummary>(RuntimeException("Server error"))
        }
        val result = handlerWith(repo).fetchBillSummary("user123")
        assert(result == BillAction.BillSummaryFailed("Server error"))
    }

    @Test
    fun `fetchBillSummary returns BillSummaryFailed with fallback when message is null`() = runTest {
        val repo = object : BillSummaryRepository {
            override suspend fun getBillSummary() =
                Result.failure<BillSummary>(RuntimeException())
        }
        val result = handlerWith(repo).fetchBillSummary("user123")
        assert(result == BillAction.BillSummaryFailed("Unknown error"))
    }
}
