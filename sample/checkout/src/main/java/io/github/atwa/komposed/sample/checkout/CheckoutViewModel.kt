package io.github.atwa.komposed.sample.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.atwa.komposed.sample.checkout.bill.presentation.BillAction
import io.github.atwa.komposed.sample.checkout.bill.presentation.BillEffectHandler
import io.github.atwa.komposed.sample.checkout.bill.presentation.billReducer
import io.github.atwa.komposed.sample.checkout.delivery.presentation.DeliveryAction
import io.github.atwa.komposed.sample.checkout.delivery.presentation.DeliveryEffectHandler
import io.github.atwa.komposed.sample.checkout.delivery.presentation.deliveryReducer
import io.github.atwa.komposed.sample.checkout.placeorder.presentation.PlaceOrderEffectHandler
import io.github.atwa.komposed.sample.checkout.placeorder.presentation.placeOrderReducer
import io.github.atwa.komposed.sample.checkout.CheckoutState.Companion.billLens
import io.github.atwa.komposed.sample.checkout.CheckoutState.Companion.deliveryLens
import io.github.atwa.komposed.sample.checkout.CheckoutState.Companion.placeOrderLens
import io.github.atwa.komposed.Navigator
import io.github.atwa.komposed.createStore
import io.github.atwa.komposed.navigationMiddleware
import io.github.atwa.komposed.reducers
import io.github.atwa.komposed.sample.core.middleware.analyticsMiddleware
import io.github.atwa.komposed.sample.core.middleware.loggingMiddleware
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val deliveryEffectHandler: DeliveryEffectHandler,
    private val billEffectHandler: BillEffectHandler,
    private val placeOrderEffectHandler: PlaceOrderEffectHandler,
    private val navigator: Navigator,
) : ViewModel() {

    val store by lazy {
        createStore(
            initialValue = CheckoutState(),
            middlewares = listOf(
                loggingMiddleware(),
                analyticsMiddleware(),
                navigationMiddleware(navigator),
            ),
            scope = viewModelScope + Dispatchers.Main.immediate,
            reducers = reducers {
                deliveryReducer.scoped(deliveryEffectHandler, deliveryLens)
                billReducer.scoped(billEffectHandler, billLens)
                placeOrderReducer.scoped(placeOrderEffectHandler, placeOrderLens)
            },
        )
    }

    init {
        store.dispatch(DeliveryAction.FetchDeliveryAddresses)
        store.dispatch(BillAction.FetchBillSummary(userId = "user123"))
    }

    fun onCheckout() = store.dispatch(store.state.value.toCheckoutAction())
}
