package io.github.atwa.komposed.app.checkout

import io.github.atwa.komposed.app.checkout.bill.BillEffectHandlerImpl
import io.github.atwa.komposed.app.checkout.bill.billKoinModule
import io.github.atwa.komposed.app.checkout.delivery.DeliveryEffectHandlerImpl
import io.github.atwa.komposed.app.checkout.delivery.deliveryKoinModule
import io.github.atwa.komposed.app.checkout.placeorder.PlaceOrderEffectHandlerImpl
import io.github.atwa.komposed.app.checkout.placeorder.placeOrderKoinModule
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val checkoutKoinModule = module {
    includes(billKoinModule, deliveryKoinModule, placeOrderKoinModule)
    viewModel {
        CheckoutViewModel(
            deliveryEffectHandler = get(qualifier<DeliveryEffectHandlerImpl>()),
            billEffectHandler = get(qualifier<BillEffectHandlerImpl>()),
            placeOrderEffectHandler = get(qualifier<PlaceOrderEffectHandlerImpl>()),
            navigator = get(),
        )
    }
}
