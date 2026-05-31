package io.github.atwa.komposed.app.orderdetails.orderinfo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun OrderInfoScreen(state: OrderInfoState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Order Confirmation",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        InfoRow(label = "Order ID", value = state.orderId)
        HorizontalDivider()
        InfoRow(label = "Total Items", value = state.totalItemsCount.toString())
        HorizontalDivider()

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = "Delivery Address",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        InfoRow(label = "Address", value = state.addressLine)
        HorizontalDivider()
        InfoRow(label = "City", value = state.city)
        HorizontalDivider()
        InfoRow(label = "Delivery Note", value = state.deliveryNote)
    }
}

@Preview
@Composable
private fun OrderInfoScreenPreview() {
    val state = OrderInfoState(
        orderId = "ORD-1716123456789",
        totalItemsCount = 3,
        addressLine = "123 Nile St",
        city = "Cairo",
        deliveryNote = "Leave at door step",
    )
    MaterialTheme {
        Surface {
            OrderInfoScreen(state = state)
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
