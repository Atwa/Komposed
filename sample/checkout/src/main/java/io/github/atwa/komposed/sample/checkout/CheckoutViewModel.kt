package io.github.atwa.komposed.sample.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.atwa.komposed.effect.EffectHandler
import io.github.atwa.komposed.navigation.Navigator
import io.github.atwa.komposed.createStore
import io.github.atwa.komposed.reducer.effectHandlers
import io.github.atwa.komposed.middleware.navigationMiddleware
import io.github.atwa.komposed.reducer.reducers
import io.github.atwa.komposed.sample.checkout.CheckoutState.Companion.billLens
import io.github.atwa.komposed.sample.checkout.CheckoutState.Companion.deliveryLens
import io.github.atwa.komposed.sample.checkout.CheckoutState.Companion.placeOrderLens
import io.github.atwa.komposed.sample.checkout.bill.presentation.BillAction
import io.github.atwa.komposed.sample.checkout.bill.presentation.BillEffect
import io.github.atwa.komposed.sample.checkout.bill.presentation.billReducer
import io.github.atwa.komposed.sample.checkout.delivery.presentation.DeliveryAction
import io.github.atwa.komposed.sample.checkout.delivery.presentation.DeliveryEffect
import io.github.atwa.komposed.sample.checkout.delivery.presentation.deliveryReducer
import io.github.atwa.komposed.sample.checkout.placeorder.presentation.PlaceOrderAction
import io.github.atwa.komposed.sample.checkout.placeorder.presentation.PlaceOrderEffect
import io.github.atwa.komposed.sample.checkout.placeorder.presentation.placeOrderReducer
import io.github.atwa.komposed.sample.core.middleware.analyticsMiddleware
import io.github.atwa.komposed.sample.core.middleware.loggingMiddleware
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val deliveryEffectHandler: EffectHandler<DeliveryEffect, DeliveryAction>,
    private val billEffectHandler: EffectHandler<BillEffect, BillAction>,
    private val placeOrderEffectHandler: EffectHandler<PlaceOrderEffect, PlaceOrderAction>,
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
            scope = viewModelScope,
            reducers = reducers {
                deliveryReducer.scoped(deliveryLens)
                billReducer.scoped(billLens)
                placeOrderReducer.scoped(placeOrderLens)
            },
            effectHandlers = effectHandlers {
                deliveryEffectHandler.register()
                billEffectHandler.register()
                placeOrderEffectHandler.register()
            },
        )
    }

    init {
        store.dispatch(DeliveryAction.FetchDeliveryAddresses)
        store.dispatch(BillAction.FetchBillSummary(userId = "user123"))
    }
}
