package de.benkralex.contacts.widgets.contactInfromationWidgets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomFieldsWidget(
    customFields: Map<String, String>,
) {
    if (customFields.isEmpty()) {
        return
    }
    for ((key, value) in customFields) {
        Card(
            modifier = Modifier
                .padding(bottom = 0.dp, top = 12.dp, end = 16.dp, start = 16.dp)
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp)
                    .align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Custom Field Icon",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(8.dp)
                        .padding(start = 16.dp)
                )
                Text(
                    text = key,
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}