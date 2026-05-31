package io.github.atwa.komposed.app.checkout.delivery.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.atwa.komposed.app.checkout.delivery.domain.DeliveryAddress

@Composable
fun DeliverySection(
    state: DeliveryState,
    dispatch: (DeliveryAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Select Delivery Address",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column {
            state.addresses.forEach { address ->
                AddressItem(
                    address = address,
                    isSelected = address.id == state.selectedAddressId,
                    onSelect = { dispatch(DeliveryAction.OnDeliveryAddressSelected(address.id)) }
                )
                HorizontalDivider()
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        OutlinedTextField(
            value = state.deliveryNote,
            onValueChange = { dispatch(DeliveryAction.OnDeliveryNoteChanged(it)) },
            label = { Text("Delivery Note") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g. Leave at the front door") }
        )
    }
}

@Composable
fun AddressItem(
    address: DeliveryAddress,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onSelect
        )
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f)
        ) {
            Text(
                text = address.addressLine,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = address.city,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (address.isDefault) {
            Text(
                text = "Default",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Preview
@Composable
private fun DeliveryScreenPreview() {
    val mockAddresses = listOf(
        DeliveryAddress(1, "123 Nile St", "Cairo", isDefault = true),
        DeliveryAddress(2, "456 Pyramid Ave", "Giza"),
        DeliveryAddress(3, "789 Alexandria Rd", "Alexandria")
    )
    val state = DeliveryState(
        addresses = mockAddresses,
        selectedAddressId = 1,
        deliveryNote = "Please ring the bell"
    )

    MaterialTheme {
        Surface {
            DeliverySection(
                state = state,
                dispatch = {}
            )
        }
    }
}
