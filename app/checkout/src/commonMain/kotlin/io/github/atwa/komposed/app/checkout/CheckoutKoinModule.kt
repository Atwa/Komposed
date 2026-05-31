package io.github.atwa.komposed.app.checkout

import io.github.atwa.komposed.app.checkout.bill.data.billKoinModule
import io.github.atwa.komposed.app.checkout.delivery.data.deliveryKoinModule
import io.github.atwa.komposed.app.checkout.placeorder.data.placeOrderKoinModule
import io.github.atwa.komposed.app.checkout.bill.data.BillEffectHandlerImpl
import io.github.atwa.komposed.app.checkout.delivery.data.DeliveryEffectHandlerImpl
import io.github.atwa.komposed.app.checkout.placeorder.data.PlaceOrderEffectHandlerImpl
import io.github.atwa.komposed.app.checkout.CheckoutViewModel
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
