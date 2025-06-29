package de.benkralex.contacts.widgets.contactInfromationWidgets

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Today
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
import de.benkralex.contacts.backend.getFormattedDate

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
                        //Log.w("EventsWidget", "ContactEvent: ${contactEvent.date} - ${contactEvent.type} - ${contactEvent.label}")
                        if (contactEvent.date.startsWith("--")) {
                            contactEvent.date = contactEvent.date.substring(1)
                        }
                        //Log.w("EventsWidget", "ContactEvent: ${contactEvent.date} - ${contactEvent.type} - ${contactEvent.label}")
                        val date = contactEvent.date.split("-").toMutableList()
                        if (date[0] == "") {
                            date[0] = "0"
                        }
                        val formattedDate = getFormattedDate(date[2].toInt(), date[1].toInt(), date[0].toInt())
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