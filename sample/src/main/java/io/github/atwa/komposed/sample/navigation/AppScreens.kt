package io.github.atwa.komposed.sample.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.atwa.komposed.sample.checkout.CheckoutScreen
import io.github.atwa.komposed.sample.checkout.CheckoutViewModel
import io.github.atwa.komposed.sample.core.navigation.CheckoutRoute
import io.github.atwa.komposed.sample.core.navigation.OrderDetailsRoute
import io.github.atwa.komposed.sample.orderdetails.OrderDetailsScreen
import io.github.atwa.komposed.sample.orderdetails.OrderDetailsViewModel

internal fun NavGraphBuilder.appScreens() {
    composable<CheckoutRoute> { entry -> CheckoutScreen(hiltViewModel<CheckoutViewModel>()) }
    composable<OrderDetailsRoute> { entry -> OrderDetailsScreen(hiltViewModel<OrderDetailsViewModel>()) }
}
