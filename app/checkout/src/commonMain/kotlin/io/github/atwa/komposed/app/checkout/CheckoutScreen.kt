package io.github.atwa.komposed.app.checkout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.atwa.komposed.app.checkout.CheckoutViewModel

@Composable
fun CheckoutScreen(viewModel: CheckoutViewModel) {
    val store = remember { viewModel.store }
    val state by store.state.collectAsStateWithLifecycle()

    CheckoutContent(state,  store::dispatch)
}
