package io.github.atwa.komposed.sample.orderdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.atwa.komposed.sample.checkout.bill.presentation.BillSection
import io.github.atwa.komposed.sample.checkout.bill.presentation.BillState
import io.github.atwa.komposed.sample.orderdetails.orderinfo.OrderInfoScreen
import io.github.atwa.komposed.sample.orderdetails.orderinfo.OrderInfoState

@Composable
fun OrderDetailsContent(state: OrderDetailsState, dispatch: (Any) -> Unit) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            OrderInfoScreen(state.orderInfoState)
            BillSection(
                state = state.billState,
                deliveryFees = state.deliveryFee,
                orderGrandTotal = state.orderGrandTotal,
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun OrderDetailsContentPreview() {
    val state = OrderDetailsState(
        orderInfoState = OrderInfoState(
            orderId = "ORD-1716123456789",
            totalItemsCount = 3,
            addressLine = "123 Nile St",
            city = "Cairo",
            deliveryNote = "Leave at door step",
        ),
        billState = BillState(serviceFees = 5.00, orderTotal = 45.00),
        deliveryFee = 5.00,
    )
    MaterialTheme {
        OrderDetailsContent(state = state, dispatch = {})
    }
}
