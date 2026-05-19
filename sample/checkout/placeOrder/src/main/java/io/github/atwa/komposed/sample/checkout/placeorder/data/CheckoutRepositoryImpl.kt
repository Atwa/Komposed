package io.github.atwa.komposed.sample.checkout.placeorder.data

import kotlinx.coroutines.delay
import javax.inject.Inject

class CheckoutRepositoryImpl @Inject constructor() : CheckoutRepository {
    override suspend fun placeOrder(request: CheckoutRequest): Result<Unit> {
        delay(1000)
        return Result.success(Unit)
    }
}
