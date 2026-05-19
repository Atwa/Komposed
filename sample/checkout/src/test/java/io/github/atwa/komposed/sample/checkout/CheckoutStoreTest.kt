package io.github.atwa.komposed.sample.checkout

import io.github.atwa.komposed.reducers
import io.github.atwa.komposed.sample.checkout.CheckoutState.Companion.billLens
import io.github.atwa.komposed.sample.checkout.CheckoutState.Companion.deliveryLens
import io.github.atwa.komposed.sample.checkout.CheckoutState.Companion.placeOrderLens
import io.github.atwa.komposed.sample.checkout.bill.domain.BillSummary
import io.github.atwa.komposed.sample.checkout.bill.presentation.BillAction
import io.github.atwa.komposed.sample.checkout.bill.presentation.BillEffectHandler
import io.github.atwa.komposed.sample.checkout.bill.presentation.BillState
import io.github.atwa.komposed.sample.checkout.bill.presentation.billReducer
import io.github.atwa.komposed.sample.checkout.delivery.domain.DeliveryAddress
import io.github.atwa.komposed.sample.checkout.delivery.presentation.DeliveryAction
import io.github.atwa.komposed.sample.checkout.delivery.presentation.DeliveryEffectHandler
import io.github.atwa.komposed.sample.checkout.delivery.presentation.DeliveryState
import io.github.atwa.komposed.sample.checkout.delivery.presentation.deliveryReducer
import io.github.atwa.komposed.sample.checkout.placeorder.presentation.PlaceOrderAction
import io.github.atwa.komposed.sample.checkout.placeorder.presentation.PlaceOrderEffectHandler
import io.github.atwa.komposed.sample.checkout.placeorder.presentation.PlaceOrderState
import io.github.atwa.komposed.sample.checkout.placeorder.presentation.placeOrderReducer
import io.github.atwa.komposed.testing.TestStore
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CheckoutStoreTest {

    private val address =
        DeliveryAddress(id = 1L, addressLine = "123 Main St", city = "Cairo", deliveryFee = 10.0)

    private val fakeDeliveryHandler = object : DeliveryEffectHandler {
        override suspend fun fetchDeliveryAddresses(): DeliveryAction =
            DeliveryAction.OnDeliveryAddressLoaded(listOf(address))
    }
    private val fakeBillHandler = object : BillEffectHandler {
        override suspend fun fetchBillSummary(userId: String): BillAction =
            BillAction.BillSummaryLoaded(
                BillSummary(
                    serviceFees = 5.0,
                    orderTotal = 100.0
                )
            )
    }
    private val fakePlaceOrderHandler = object : PlaceOrderEffectHandler {
        override suspend fun placeOrder(
            addressId: Long, deliveryNote: String, addressLine: String,
            city: String, deliveryFees: Double, serviceFees: Double, orderTotal: Double,
        ): PlaceOrderAction = PlaceOrderAction.OrderPlaced(
            orderId = "order-1",
            addressLine = addressLine,
            city = city,
            deliveryNote = deliveryNote,
            deliveryFee = deliveryFees,
            serviceFees = serviceFees,
            orderTotal = orderTotal,
        )
    }

    private fun buildStore(scope: kotlinx.coroutines.test.TestScope) = TestStore(
        initialState = CheckoutState(),
        reducers = reducers {
            deliveryReducer.scoped(fakeDeliveryHandler, deliveryLens)
            billReducer.scoped(fakeBillHandler, billLens)
            placeOrderReducer.scoped(fakePlaceOrderHandler, placeOrderLens)
        },
        scope = scope,
    )

    @Test
    fun `initial state is empty`() = runTest {
        buildStore(this).assertState(CheckoutState())
    }

    @Test
    fun `OnDeliveryAddressSelected routes to delivery reducer`() = runTest {
        val store = buildStore(this)
        store.dispatch(DeliveryAction.OnDeliveryAddressSelected(1L))
        store.assertState(CheckoutState(deliveryState = DeliveryState(selectedAddressId = 1L)))
    }

    @Test
    fun `OnDeliveryNoteChanged routes to delivery reducer`() = runTest {
        val store = buildStore(this)
        store.dispatch(DeliveryAction.OnDeliveryNoteChanged("Leave at door"))
        store.assertState(CheckoutState(deliveryState = DeliveryState(deliveryNote = "Leave at door")))
    }

    @Test
    fun `FetchDeliveryAddresses effect loads addresses into state`() = runTest {
        val store = buildStore(this)
        store.dispatch(DeliveryAction.FetchDeliveryAddresses)
        advanceUntilIdle()
        assert(store.state.value.deliveryState.addresses == listOf(address))
        assert(!store.state.value.deliveryState.isLoading)
    }

    @Test
    fun `FetchBillSummary effect loads fees into state`() = runTest {
        val store = buildStore(this)
        store.dispatch(BillAction.FetchBillSummary("user123"))
        advanceUntilIdle()
        assert(store.state.value.billState == BillState(serviceFees = 5.0, orderTotal = 100.0))
    }

    @Test
    fun `Checkout with null address sets error on placeOrder state`() = runTest {
        val store = buildStore(this)
        store.dispatch(CheckoutState().toCheckoutAction())
        store.assertState(
            CheckoutState(placeOrderState = PlaceOrderState(errorMessage = "Please select a delivery address"))
        )
    }

    @Test
    fun `Checkout with valid address sets in-progress then dispatches OrderPlaced effect`() =
        runTest {
            val store = buildStore(this)
            store.dispatch(DeliveryAction.OnDeliveryAddressSelected(1L))
            store.dispatch(
                CheckoutState(
                    deliveryState = DeliveryState(
                        addresses = listOf(address),
                        selectedAddressId = 1L
                    ),
                    billState = BillState(serviceFees = 5.0, orderTotal = 100.0),
                ).toCheckoutAction()
            )
            advanceUntilIdle()
            assert(!store.state.value.placeOrderState.isCheckoutInProgress)
            assert(store.state.value.placeOrderState.errorMessage == null)
        }
}
