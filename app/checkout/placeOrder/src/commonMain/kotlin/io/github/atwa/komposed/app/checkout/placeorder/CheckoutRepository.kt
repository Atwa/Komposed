package io.github.atwa.komposed.app.checkout.placeorder

interface CheckoutRepository {
    suspend fun placeOrder(request: CheckoutRequest): Result<Unit>
}
