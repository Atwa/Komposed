package io.github.atwa.komposed.sample.checkout.placeorder.presentation

import io.github.atwa.komposed.effect.Effect

sealed interface PlaceOrderEffect : Effect {
    data class PlaceOrder(val params: CheckoutParams) : PlaceOrderEffect
}
