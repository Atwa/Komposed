package io.github.atwa.komposed.sample.checkout.placeorder.data

import io.github.atwa.komposed.effect.EffectHandler
import io.github.atwa.komposed.sample.checkout.placeorder.presentation.PlaceOrderAction
import io.github.atwa.komposed.sample.checkout.placeorder.presentation.PlaceOrderEffect
import javax.inject.Inject

class PlaceOrderEffectHandlerImpl @Inject constructor(
    private val repository: CheckoutRepository,
) : EffectHandler<PlaceOrderEffect, PlaceOrderAction> {

    override suspend fun handle(effect: PlaceOrderEffect, dispatch: suspend (suspend () -> PlaceOrderAction) -> Unit) {
        when (effect) {
            is PlaceOrderEffect.PlaceOrder -> {
                val params = effect.params
                val addressId = params.addressId ?: return
                dispatch {
                    repository.placeOrder(
                        CheckoutRequest(
                            addressId = addressId,
                            deliveryNote = params.deliveryNote,
                            serviceFees = params.serviceFees,
                            orderTotal = params.orderTotal,
                            deliveryFees = params.deliveryFees,
                        )
                    ).fold(
                        onSuccess = {
                            PlaceOrderAction.OrderPlaced(
                                orderId = "ORD-${System.currentTimeMillis()}",
                                addressLine = params.addressLine,
                                city = params.city,
                                deliveryNote = params.deliveryNote,
                                deliveryFee = params.deliveryFees,
                                serviceFees = params.serviceFees,
                                orderTotal = params.orderTotal,
                            )
                        },
                        onFailure = { PlaceOrderAction.CheckoutFailed(it.message ?: "Unknown error") },
                    )
                }
            }
        }
    }
}
