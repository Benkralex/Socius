package de.benkralex.contacts.widgets.contactInfromationWidgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import de.benkralex.contacts.backend.ContactEvent

@Composable
fun EventsWidget(
    contactEvents: List<ContactEvent>
) {
    if (contactEvents.isEmpty()) {
        return
    }
    val context = LocalContext.current
    Card (
        modifier = Modifier
            .padding(bottom = 0.dp, top = 12.dp, end = 16.dp, start = 16.dp)
            .fillMaxWidth(),
    ) {
        Column {
            contactEvents.forEach { contactEvent ->
                Row {
                    Icon(
                        imageVector = Icons.Outlined.DateRange,
                        contentDescription = "Date",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(8.dp)
                            .padding(start = 16.dp)
                    )
                    Column (
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(8.dp),
                    ) {
                        var date = contactEvent.date.split("-")
                        if (date.size > 3) {
                            date = date.subList(1, 4)
                        }
                        var formattedDate = date[2] + "." + date[1] + "."
                        if (!date[0].isEmpty()) {
                            formattedDate += date[0]
                        }
                        Text(
                            text = formattedDate,
                        )
                        Text(
                            text = translateType(contactEvent.type, contactEvent.label),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
                if (contactEvents.indexOf(contactEvent) != (contactEvents.size - 1)) HorizontalDivider(
                    thickness = 2.dp,
                )
            }
        }
    }
}