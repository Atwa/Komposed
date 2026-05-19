package io.github.atwa.komposed.sample.checkout

import io.github.atwa.komposed.sample.checkout.bill.presentation.BillState
import io.github.atwa.komposed.sample.checkout.delivery.domain.DeliveryAddress
import io.github.atwa.komposed.sample.checkout.delivery.presentation.DeliveryState
import io.github.atwa.komposed.sample.checkout.placeorder.presentation.PlaceOrderAction
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
    fun `toCheckoutAction maps all state fields correctly`() {
        val state = CheckoutState(
            deliveryState = DeliveryState(
                addresses = listOf(address),
                selectedAddressId = 1L,
                deliveryNote = "Ring bell",
            ),
            billState = BillState(serviceFees = 5.0, orderTotal = 100.0),
        )
        val action = state.toCheckoutAction()
        assert(action is PlaceOrderAction.Checkout)
        assert(action.selectedAddressId == 1L)
        assert(action.deliveryNote == "Ring bell")
        assert(action.addressLine == "123 Main St")
        assert(action.city == "Cairo")
        assert(action.deliveryFees == 10.0)
        assert(action.serviceFees == 5.0)
        assert(action.orderTotal == 100.0)
    }

    @Test
    fun `toCheckoutAction uses empty strings when no address is selected`() {
        val action = CheckoutState().toCheckoutAction()
        assert(action.selectedAddressId == null)
        assert(action.addressLine == "")
        assert(action.city == "")
    }
}
