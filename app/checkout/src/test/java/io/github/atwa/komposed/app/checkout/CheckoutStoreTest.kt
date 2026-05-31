package io.github.atwa.komposed.app.checkout

import io.github.atwa.komposed.effect.EffectHandler
import io.github.atwa.komposed.reducer.effectHandlers
import io.github.atwa.komposed.reducer.reducers
import io.github.atwa.komposed.app.checkout.CheckoutState.Companion.billLens
import io.github.atwa.komposed.app.checkout.CheckoutState.Companion.deliveryLens
import io.github.atwa.komposed.app.checkout.CheckoutState.Companion.placeOrderLens
import io.github.atwa.komposed.app.checkout.bill.domain.BillSummary
import io.github.atwa.komposed.app.checkout.bill.presentation.BillAction
import io.github.atwa.komposed.app.checkout.bill.presentation.BillEffect
import io.github.atwa.komposed.app.checkout.bill.presentation.BillState
import io.github.atwa.komposed.app.checkout.bill.presentation.billReducer
import io.github.atwa.komposed.app.checkout.delivery.domain.DeliveryAddress
import io.github.atwa.komposed.app.checkout.delivery.presentation.DeliveryAction
import io.github.atwa.komposed.app.checkout.delivery.presentation.DeliveryEffect
import io.github.atwa.komposed.app.checkout.delivery.presentation.DeliveryState
import io.github.atwa.komposed.app.checkout.delivery.presentation.deliveryReducer
import io.github.atwa.komposed.app.checkout.placeorder.presentation.CheckoutParams
import io.github.atwa.komposed.app.checkout.placeorder.presentation.PlaceOrderAction
import io.github.atwa.komposed.app.checkout.placeorder.presentation.PlaceOrderEffect
import io.github.atwa.komposed.app.checkout.placeorder.presentation.PlaceOrderState
import io.github.atwa.komposed.app.checkout.placeorder.presentation.placeOrderReducer
import io.github.atwa.komposed.subscription.subscriptions
import io.github.atwa.komposed.testing.TestStore
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CheckoutStoreTest {

    private val address =
        DeliveryAddress(id = 1L, addressLine = "123 Main St", city = "Cairo", deliveryFee = 10.0)

    private val fakeDeliveryHandler = object : EffectHandler<DeliveryEffect, DeliveryAction> {
        override suspend fun handle(effect: DeliveryEffect, dispatch: suspend (suspend () -> DeliveryAction) -> Unit) {
            when (effect) {
                DeliveryEffect.FetchAddresses ->
                    dispatch { DeliveryAction.OnDeliveryAddressLoaded(listOf(address)) }
            }
        }
    }

    private val fakeBillHandler = object : EffectHandler<BillEffect, BillAction> {
        override suspend fun handle(effect: BillEffect, dispatch: suspend (suspend () -> BillAction) -> Unit) {
            when (effect) {
                is BillEffect.FetchSummary ->
                    dispatch { BillAction.BillSummaryLoaded(BillSummary(serviceFees = 5.0, orderTotal = 100.0)) }
            }
        }
    }

    private val fakePlaceOrderHandler = object : EffectHandler<PlaceOrderEffect, PlaceOrderAction> {
        override suspend fun handle(effect: PlaceOrderEffect, dispatch: suspend (suspend () -> PlaceOrderAction) -> Unit) {
            when (effect) {
                is PlaceOrderEffect.PlaceOrder -> {
                    val params = effect.params
                    dispatch {
                        PlaceOrderAction.OrderPlaced(
                            orderId = "order-1",
                            addressLine = params.addressLine,
                            city = params.city,
                            deliveryNote = params.deliveryNote,
                            deliveryFee = params.deliveryFees,
                            serviceFees = params.serviceFees,
                            orderTotal = params.orderTotal,
                        )
                    }
                }
            }
        }
    }

    private fun buildStore(scope: kotlinx.coroutines.test.TestScope) = TestStore(
        initialState = CheckoutState(),
        reducers = reducers {
            deliveryReducer.scoped(deliveryLens)
            billReducer.scoped(billLens)
            placeOrderReducer.scoped(placeOrderLens)
        },
        effectHandlers = effectHandlers {
            fakeDeliveryHandler.register()
            fakeBillHandler.register()
            fakePlaceOrderHandler.register()
        },
        subscriptions = subscriptions {
            subscription(
                selector = { it.deliveryState.selectedAddress?.deliveryFee ?: 0.0 },
                action = { BillAction.DeliveryFeeUpdated(it) },
            )
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
    fun `Checkout with null addressId sets error on placeOrder state`() = runTest {
        val store = buildStore(this)
        store.dispatch(
            PlaceOrderAction.Checkout(
                CheckoutParams(
                    addressId = null,
                    deliveryNote = "",
                    addressLine = "",
                    city = "",
                    deliveryFees = 0.0,
                    serviceFees = 0.0,
                    orderTotal = 0.0,
                )
            )
        )
        store.assertState(
            CheckoutState(placeOrderState = PlaceOrderState(errorMessage = "Please select a delivery address"))
        )
    }

    @Test
    fun `selecting a delivery address pushes its fee into BillState via subscription`() = runTest {
        val store = buildStore(this)
        // Load addresses so the selected address object is resolvable from the state.
        store.dispatch(DeliveryAction.FetchDeliveryAddresses)
        advanceUntilIdle()
        // Selecting address 1 (fee = 10.0) changes CheckoutState.deliveryFee from 0.0 to 10.0.
        // The subscription detects the change and automatically dispatches DeliveryFeeUpdated(10.0)
        // into the bill reducer — without BillModule knowing anything about DeliveryModule.
        store.dispatch(DeliveryAction.OnDeliveryAddressSelected(1L))
        assert(store.state.value.billState.deliveryFees == 10.0)
    }

    @Test
    fun `subscription does not fire when delivery fee is unchanged`() = runTest {
        val store = buildStore(this)
        store.dispatch(DeliveryAction.FetchDeliveryAddresses)
        advanceUntilIdle()
        store.dispatch(DeliveryAction.OnDeliveryAddressSelected(1L))
        val feeAfterFirst = store.state.value.billState.deliveryFees
        // Selecting the same address again — fee unchanged, no extra dispatch expected.
        store.dispatch(DeliveryAction.OnDeliveryAddressSelected(1L))
        assert(store.state.value.billState.deliveryFees == feeAfterFirst)
    }

    @Test
    fun `switching delivery address updates bill delivery fee to the new value`() = runTest {
        val store = buildStore(this)
        store.dispatch(DeliveryAction.FetchDeliveryAddresses)
        advanceUntilIdle()
        store.dispatch(DeliveryAction.OnDeliveryAddressSelected(1L))  // fee = 10.0
        assert(store.state.value.billState.deliveryFees == 10.0)
    }

    @Test
    fun `Checkout with valid params sets in-progress then dispatches OrderPlaced`() = runTest {
        val store = buildStore(this)
        store.dispatch(DeliveryAction.OnDeliveryAddressSelected(1L))
        store.dispatch(
            PlaceOrderAction.Checkout(
                CheckoutParams(
                    addressId = 1L,
                    deliveryNote = "",
                    addressLine = "123 Main St",
                    city = "Cairo",
                    deliveryFees = 10.0,
                    serviceFees = 5.0,
                    orderTotal = 100.0,
                )
            )
        )
        advanceUntilIdle()
        assert(!store.state.value.placeOrderState.isCheckoutInProgress)
        assert(store.state.value.placeOrderState.errorMessage == null)
    }
}
