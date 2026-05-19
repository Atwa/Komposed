package io.github.atwa.komposed.sample.checkout.placeorder.data

import io.github.atwa.komposed.sample.checkout.placeorder.presentation.PlaceOrderAction
import io.github.atwa.komposed.sample.checkout.placeorder.presentation.PlaceOrderEffectHandler
import javax.inject.Inject

class PlaceOrderEffectHandlerImpl @Inject constructor(
    private val repository: CheckoutRepository,
) : PlaceOrderEffectHandler {

    override suspend fun placeOrder(
        addressId: Long,
        deliveryNote: String,
        addressLine: String,
        city: String,
        deliveryFees: Double,
        serviceFees: Double,
        orderTotal: Double,
    ): PlaceOrderAction = repository.placeOrder(
        CheckoutRequest(
            addressId = addressId,
            deliveryNote = deliveryNote,
            serviceFees = serviceFees,
            orderTotal = orderTotal,
            deliveryFees = deliveryFees,
        )
    ).fold(
        onSuccess = {
            PlaceOrderAction.OrderPlaced(
                orderId = "ORD-${System.currentTimeMillis()}",
                addressLine = addressLine,
                city = city,
                deliveryNote = deliveryNote,
                deliveryFee = deliveryFees,
                serviceFees = serviceFees,
                orderTotal = orderTotal,
            )
        },
        onFailure = { PlaceOrderAction.CheckoutFailed(it.message ?: "Unknown error") },
    )
}
