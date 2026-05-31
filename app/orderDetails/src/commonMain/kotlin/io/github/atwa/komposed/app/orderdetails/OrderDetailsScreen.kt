package io.github.atwa.komposed.app.orderdetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun OrderDetailsScreen(viewModel: OrderDetailsViewModel) {
    val store = remember { viewModel.store }
    val state by store.state.collectAsStateWithLifecycle()
    OrderDetailsContent(state, store::dispatch)
}
