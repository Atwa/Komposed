package io.github.atwa.komposed.sample.checkout.delivery.presentation

import io.github.atwa.komposed.effect.Effect

sealed interface DeliveryEffect : Effect {
    data object FetchAddresses : DeliveryEffect
}
