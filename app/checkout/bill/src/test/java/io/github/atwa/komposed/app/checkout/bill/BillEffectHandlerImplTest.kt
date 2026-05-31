package io.github.atwa.komposed.app.checkout.bill

import io.github.atwa.komposed.testing.handle
import kotlinx.coroutines.test.runTest
import org.junit.Test

class BillEffectHandlerImplTest {

    private fun handlerWith(repository: BillSummaryRepository) = BillEffectHandlerImpl(repository)

    @Test
    fun `FetchSummary dispatches BillSummaryLoaded on success`() = runTest {
        val summary = BillSummary(serviceFees = 5.0, orderTotal = 100.0)
        val repo = object : BillSummaryRepository {
            override suspend fun getBillSummary() = Result.success(summary)
        }
        val result = handlerWith(repo).handle(BillEffect.FetchSummary("user123"))
        assert(result.single() == BillAction.BillSummaryLoaded(summary))
    }

    @Test
    fun `FetchSummary dispatches BillSummaryFailed with message on failure`() = runTest {
        val repo = object : BillSummaryRepository {
            override suspend fun getBillSummary() =
                Result.failure<BillSummary>(RuntimeException("Server error"))
        }
        val result = handlerWith(repo).handle(BillEffect.FetchSummary("user123"))
        assert(result.single() == BillAction.BillSummaryFailed("Server error"))
    }

    @Test
    fun `FetchSummary dispatches BillSummaryFailed with fallback when message is null`() = runTest {
        val repo = object : BillSummaryRepository {
            override suspend fun getBillSummary() =
                Result.failure<BillSummary>(RuntimeException())
        }
        val result = handlerWith(repo).handle(BillEffect.FetchSummary("user123"))
        assert(result.single() == BillAction.BillSummaryFailed("Unknown error"))
    }
}
