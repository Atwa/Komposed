package io.github.atwa.komposed.app.checkout.bill.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.round

private fun formatAmount(amount: Double): String {
    val cents = round(amount * 100).toLong()
    val dollars = cents / 100
    val frac = cents % 100
    return "$dollars.${frac.toString().padStart(2, '0')}"
}

@Composable
fun BillSection(
    state: BillState,
    deliveryFees: Double,
    orderGrandTotal: Double
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Order Summary",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        BillRow(label = "Delivery Fees", amount = deliveryFees)
        HorizontalDivider()
        BillRow(label = "Service Fees", amount = state.serviceFees)
        HorizontalDivider()
        BillRow(label = "Order", amount = state.orderTotal)
        HorizontalDivider()
        BillRow(
            label = "Order Grand Total",
            amount = orderGrandTotal,
            labelStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
private fun BillRow(
    label: String,
    amount: Double,
    labelStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = labelStyle)
        Text(text = "$${formatAmount(amount)}", style = labelStyle)
    }
}

@Preview
@Composable
private fun BillScreenPreview() {
    val state = BillState(serviceFees = 5.00, orderTotal = 45.00)
    MaterialTheme {
        Surface {
            BillSection(state = state, deliveryFees = 7.50, orderGrandTotal = 57.50)
        }
    }
}
