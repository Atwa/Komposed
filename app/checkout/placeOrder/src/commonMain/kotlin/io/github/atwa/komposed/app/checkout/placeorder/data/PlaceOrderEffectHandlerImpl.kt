package io.github.atwa.komposed.app.checkout.placeorder.data

import io.github.atwa.komposed.effect.EffectHandler
import io.github.atwa.komposed.app.checkout.placeorder.presentation.PlaceOrderAction
import io.github.atwa.komposed.app.checkout.placeorder.presentation.PlaceOrderEffect
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
@OptIn(ExperimentalTime::class)
class PlaceOrderEffectHandlerImpl(
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
                                orderId = "ORD-${Clock.System.now().toEpochMilliseconds()}",
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
