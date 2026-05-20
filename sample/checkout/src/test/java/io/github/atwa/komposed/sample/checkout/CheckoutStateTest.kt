package io.github.atwa.komposed.sample.checkout

import io.github.atwa.komposed.sample.checkout.bill.presentation.BillState
import io.github.atwa.komposed.sample.checkout.delivery.domain.DeliveryAddress
import io.github.atwa.komposed.sample.checkout.delivery.presentation.DeliveryState
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
    fun `deliveryFee returns selected address fee`() {
        val state = CheckoutState(
            deliveryState = DeliveryState(addresses = listOf(address), selectedAddressId = 1L)
        )
        assert(state.deliveryFee == 10.0)
    }

    @Test
    fun `deliveryFee is 0 when no address selected`() {
        assert(CheckoutState().deliveryFee == 0.0)
    }

    @Test
    fun `orderGrandTotal sums orderTotal, serviceFees and deliveryFee`() {
        val state = CheckoutState(
            billState = BillState(orderTotal = 100.0, serviceFees = 5.0),
            deliveryState = DeliveryState(addresses = listOf(address), selectedAddressId = 1L),
        )
        assert(state.orderGrandTotal == 115.0)
    }

    @Test
    fun `toCheckoutParams maps all state fields correctly`() {
        val state = CheckoutState(
            deliveryState = DeliveryState(
                addresses = listOf(address),
                selectedAddressId = 1L,
                deliveryNote = "Ring bell",
            ),
            billState = BillState(serviceFees = 5.0, orderTotal = 100.0),
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
