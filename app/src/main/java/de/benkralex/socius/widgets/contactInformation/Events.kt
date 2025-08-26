package de.benkralex.socius.widgets.contactInformation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.benkralex.socius.data.ContactEvent
import de.benkralex.socius.data.settings.getFormattedDate
import de.benkralex.socius.widgets.contactInformation.helpers.translateType

@Composable
fun EventsWidget(
    contactEvents: List<ContactEvent>
) {
    if (contactEvents.isEmpty()) {
        return
    }
    Card (
        modifier = Modifier
            .padding(bottom = 0.dp, top = 12.dp, end = 16.dp, start = 16.dp)
            .fillMaxWidth(),
    ) {
        Column {
            contactEvents.forEach { contactEvent ->
                Row {
                    if (contactEvent.type == "birthday") {
                        Icon(
                            imageVector = Icons.Outlined.Cake,
                            contentDescription = "Cake",
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(8.dp)
                                .padding(start = 16.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.DateRange,
                            contentDescription = "Date",
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(8.dp)
                                .padding(start = 16.dp)
                        )
                    }
                    Column (
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(8.dp),
                    ) {
                        val formattedDate = getFormattedDate(contactEvent.day ?: 0, contactEvent.month ?: 0, contactEvent.year ?: 0)
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