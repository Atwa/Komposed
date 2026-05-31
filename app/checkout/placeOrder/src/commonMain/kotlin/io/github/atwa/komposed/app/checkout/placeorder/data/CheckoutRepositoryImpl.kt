package io.github.atwa.komposed.app.checkout.placeorder.data

import kotlinx.coroutines.delay

class CheckoutRepositoryImpl : CheckoutRepository {
    override suspend fun placeOrder(request: CheckoutRequest): Result<Unit> {
        delay(1000)
        return Result.success(Unit)
    }
}
