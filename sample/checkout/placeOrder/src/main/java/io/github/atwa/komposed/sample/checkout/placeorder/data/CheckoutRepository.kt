package io.github.atwa.komposed.sample.checkout.placeorder.data

interface CheckoutRepository {
    suspend fun placeOrder(request: CheckoutRequest): Result<Unit>
}
