package io.github.atwa.komposed.app.checkout

import io.github.atwa.komposed.app.checkout.bill.presentation.BillState
import io.github.atwa.komposed.app.checkout.delivery.domain.DeliveryAddress
import io.github.atwa.komposed.app.checkout.delivery.presentation.DeliveryState
import org.junit.Test

class CheckoutStateTest {

    private val address = DeliveryAddress(id = 1L, addressLine = "123 Main St", city = "Cairo", deliveryFee = 10.0)

    @Test
    fun `isLoading is true when delivery is loading`() {
        val state = CheckoutState(deliveryState = DeliveryState(isLoading = true))
        assert(state.isLoading)
    }

    @Test
    fun `isLoading is true when bill is loading`() {
        val state = CheckoutState(billState = BillState(isLoading = true))
        assert(state.isLoading)
    }

    @Test
    fun `isLoading is false when neither is loading`() {
        assert(!CheckoutState().isLoading)
    }

    @Test
    fun `toCheckoutParams maps all state fields correctly`() {
        val state = CheckoutState(
            deliveryState = DeliveryState(
                addresses = listOf(address),
                selectedAddressId = 1L,
                deliveryNote = "Ring bell",
            ),
            billState = BillState(serviceFees = 5.0, orderTotal = 100.0, deliveryFees = 10.0),
        )
        val params = state.toCheckoutParams()
        assert(params.addressId == 1L)
        assert(params.deliveryNote == "Ring bell")
        assert(params.addressLine == "123 Main St")
        assert(params.city == "Cairo")
        assert(params.deliveryFees == 10.0)
        assert(params.serviceFees == 5.0)
        assert(params.orderTotal == 100.0)
    }

    @Test
    fun `toCheckoutParams has null addressId when no address is selected`() {
        assert(CheckoutState().toCheckoutParams().addressId == null)
    }
}
