package io.github.atwa.komposed.sample.checkout.delivery.presentation

import io.github.atwa.komposed.sample.checkout.delivery.domain.DeliveryAddress
import io.github.atwa.komposed.testing.assertEffect
import io.github.atwa.komposed.testing.assertNoEffect
import io.github.atwa.komposed.testing.assertNoStateChange
import io.github.atwa.komposed.testing.assertState
import io.github.atwa.komposed.testing.given
import org.junit.Test

class DeliveryReducerTest {

    private val address = DeliveryAddress(id = 1L, addressLine = "123 Main St", city = "Cairo", deliveryFee = 10.0)

    @Test
    fun `FetchDeliveryAddresses sets isLoading and emits FetchAddresses effect`() {
        deliveryReducer.given(DeliveryState(), DeliveryAction.FetchDeliveryAddresses)
            .assertState(DeliveryState(isLoading = true, error = null))
            .assertEffect<DeliveryEffect.FetchAddresses>()
    }

    @Test
    fun `OnDeliveryAddressSelected updates selectedAddressId with no effect`() {
        deliveryReducer.given(DeliveryState(), DeliveryAction.OnDeliveryAddressSelected(1L))
            .assertState(DeliveryState(selectedAddressId = 1L))
            .assertNoEffect()
    }

    @Test
    fun `OnDeliveryNoteChanged updates deliveryNote with no effect`() {
        deliveryReducer.given(DeliveryState(), DeliveryAction.OnDeliveryNoteChanged("Leave at door"))
            .assertState(DeliveryState(deliveryNote = "Leave at door"))
            .assertNoEffect()
    }

    @Test
    fun `OnDeliveryAddressLoaded populates addresses and clears isLoading`() {
        val initial = DeliveryState(isLoading = true)
        deliveryReducer.given(initial, DeliveryAction.OnDeliveryAddressLoaded(listOf(address)))
            .assertState(DeliveryState(addresses = listOf(address), isLoading = false))
            .assertNoEffect()
    }

    @Test
    fun `OnDeliveryAddressFailure sets error and clears isLoading`() {
        val initial = DeliveryState(isLoading = true)
        deliveryReducer.given(initial, DeliveryAction.OnDeliveryAddressFailure("Network error"))
            .assertState(DeliveryState(isLoading = false, error = "Network error"))
            .assertNoEffect()
    }

    @Test
    fun `selectedAddress resolves from addresses list by selectedAddressId`() {
        val state = DeliveryState(addresses = listOf(address), selectedAddressId = 1L)
        assert(state.selectedAddress == address)
    }

    @Test
    fun `selectedAddress is null when no address matches`() {
        val state = DeliveryState(addresses = listOf(address), selectedAddressId = 99L)
        assert(state.selectedAddress == null)
    }
}
