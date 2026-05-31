package io.github.atwa.komposed.app.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.atwa.komposed.app.checkout.CheckoutState.Companion.billLens
import io.github.atwa.komposed.app.checkout.CheckoutState.Companion.deliveryLens
import io.github.atwa.komposed.app.checkout.CheckoutState.Companion.placeOrderLens
import io.github.atwa.komposed.app.checkout.bill.BillAction
import io.github.atwa.komposed.app.checkout.bill.BillEffect
import io.github.atwa.komposed.app.checkout.bill.billReducer
import io.github.atwa.komposed.app.checkout.delivery.DeliveryAction
import io.github.atwa.komposed.app.checkout.delivery.DeliveryEffect
import io.github.atwa.komposed.app.checkout.delivery.deliveryReducer
import io.github.atwa.komposed.app.checkout.placeorder.PlaceOrderAction
import io.github.atwa.komposed.app.checkout.placeorder.PlaceOrderEffect
import io.github.atwa.komposed.app.checkout.placeorder.placeOrderReducer
import io.github.atwa.komposed.app.core.middleware.analyticsMiddleware
import io.github.atwa.komposed.app.core.middleware.loggingMiddleware
import io.github.atwa.komposed.createStore
import io.github.atwa.komposed.effect.EffectHandler
import io.github.atwa.komposed.middleware.navigationMiddleware
import io.github.atwa.komposed.navigation.Navigator
import io.github.atwa.komposed.reducer.effectHandlers
import io.github.atwa.komposed.reducer.reducers
import io.github.atwa.komposed.subscription.subscriptions

class CheckoutViewModel(
    private val deliveryEffectHandler: EffectHandler<DeliveryEffect, DeliveryAction>,
    private val billEffectHandler: EffectHandler<BillEffect, BillAction>,
    private val placeOrderEffectHandler: EffectHandler<PlaceOrderEffect, PlaceOrderAction>,
    private val navigator: Navigator,
) : ViewModel() {
    val store = createStore(
        initialValue = CheckoutState(),
        scope = viewModelScope,
        middlewares = listOf(
            loggingMiddleware(),
            analyticsMiddleware(),
            navigationMiddleware(navigator),
        ),
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
        subscriptions = subscriptions {
            subscription(
                selector = { it.deliveryState.selectedAddress?.deliveryFee ?: 0.0 },
                action = { BillAction.DeliveryFeeUpdated(it) },
            )
        },
    ).apply {
        dispatch(DeliveryAction.FetchDeliveryAddresses)
        dispatch(BillAction.FetchBillSummary(userId = "user123"))
    }
}
