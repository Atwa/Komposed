package io.github.atwa.komposed.app.orderdetails

import io.github.atwa.komposed.app.core.navigation.OrderDetailsRoute
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val orderDetailsKoinModule = module {
    viewModel { params -> OrderDetailsViewModel(params.get<OrderDetailsRoute>()) }
}
