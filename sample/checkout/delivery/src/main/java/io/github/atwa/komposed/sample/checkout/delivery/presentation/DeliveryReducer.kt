package io.github.atwa.komposed.sample.checkout.delivery.presentation

import io.github.atwa.komposed.sample.checkout.delivery.domain.DeliveryAddress
import io.github.atwa.komposed.ActionableEffect
import io.github.atwa.komposed.ReduceType.Companion.reduce
import io.github.atwa.komposed.ReduceType.Companion.withEffect
import io.github.atwa.komposed.reducer

data class DeliveryState(
    val addresses: List<DeliveryAddress> = emptyList(),
    val selectedAddressId: Long? = null,
    val deliveryNote: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
) {
    val selectedAddress
        get() = addresses.find { it.id == selectedAddressId }
}

sealed interface DeliveryAction {
    data class OnDeliveryAddressSelected(val addressId: Long) : DeliveryAction
    data class OnDeliveryNoteChanged(val note: String) : DeliveryAction
    data object FetchDeliveryAddresses : DeliveryAction
    data class OnDeliveryAddressLoaded(val addresses: List<DeliveryAddress>) : DeliveryAction
    data class OnDeliveryAddressFailure(val message: String) : DeliveryAction
}

val deliveryReducer =
    reducer<DeliveryState, DeliveryAction, DeliveryEffectHandler> { state, action, handler ->
        when (action) {
            is DeliveryAction.OnDeliveryAddressSelected ->
                state.copy(selectedAddressId = action.addressId).reduce()

            is DeliveryAction.OnDeliveryNoteChanged ->
                state.copy(deliveryNote = action.note).reduce()

            is DeliveryAction.FetchDeliveryAddresses -> state.copy(isLoading = true, error = null)
                .withEffect {
                    ActionableEffect { handler.fetchDeliveryAddresses() }
                }

            is DeliveryAction.OnDeliveryAddressFailure ->
                state.copy(isLoading = false, error = action.message).reduce()

            is DeliveryAction.OnDeliveryAddressLoaded ->
                state.copy(addresses = action.addresses, isLoading = false, error = null).reduce()
        }
    }
