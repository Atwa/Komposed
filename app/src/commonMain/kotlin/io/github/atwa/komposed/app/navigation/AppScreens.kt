package io.github.atwa.komposed.app.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import io.github.atwa.komposed.app.checkout.CheckoutScreen
import io.github.atwa.komposed.app.checkout.CheckoutViewModel
import io.github.atwa.komposed.app.core.navigation.CheckoutRoute
import io.github.atwa.komposed.app.core.navigation.OrderDetailsRoute
import io.github.atwa.komposed.app.orderdetails.OrderDetailsScreen
import io.github.atwa.komposed.app.orderdetails.OrderDetailsViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

internal fun NavGraphBuilder.appScreens() {
    composable<CheckoutRoute> { CheckoutScreen(koinViewModel<CheckoutViewModel>()) }
    composable<OrderDetailsRoute> { entry ->
        val route = entry.toRoute<OrderDetailsRoute>()
        OrderDetailsScreen(koinViewModel<OrderDetailsViewModel> { parametersOf(route) })
    }
}
