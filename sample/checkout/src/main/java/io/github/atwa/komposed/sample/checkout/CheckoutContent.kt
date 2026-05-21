package io.github.atwa.komposed.sample.checkout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.atwa.komposed.sample.checkout.bill.presentation.BillSection
import io.github.atwa.komposed.sample.checkout.delivery.presentation.DeliverySection
import io.github.atwa.komposed.sample.checkout.placeorder.presentation.PlaceOrderAction
import io.github.atwa.komposed.sample.checkout.placeorder.presentation.PlaceOrderSection

@Composable
fun CheckoutContent(
    state: CheckoutState,
    dispatch: (Any) -> Unit
) {
    Scaffold(
        bottomBar = {
            PlaceOrderSection(
                state = state.placeOrderState,
                isLoading = state.isLoading,
                onCheckout = { dispatch(PlaceOrderAction.Checkout(state.toCheckoutParams())) },
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    DeliverySection(state.deliveryState, dispatch)
                    BillSection(
                        state = state.billState,
                        deliveryFees = state.billState.deliveryFees,
                        orderGrandTotal = state.billState.grandTotal,
                    )
                }
            }

            AnimatedVisibility(
                visible = state.deliveryState.error != null,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                CheckoutErrorScreen(message = state.deliveryState.error ?: "")
            }
        }
    }
}
