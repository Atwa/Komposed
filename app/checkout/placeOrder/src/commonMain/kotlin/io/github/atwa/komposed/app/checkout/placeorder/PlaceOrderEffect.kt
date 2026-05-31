package io.github.atwa.komposed.app.checkout.placeorder

import io.github.atwa.komposed.effect.Effect

sealed interface PlaceOrderEffect : Effect {
    data class PlaceOrder(val params: CheckoutParams) : PlaceOrderEffect
}
