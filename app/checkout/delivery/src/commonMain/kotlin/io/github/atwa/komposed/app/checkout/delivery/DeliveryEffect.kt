package io.github.atwa.komposed.app.checkout.delivery

import io.github.atwa.komposed.effect.Effect

sealed interface DeliveryEffect : Effect {
    data object FetchAddresses : DeliveryEffect
}
