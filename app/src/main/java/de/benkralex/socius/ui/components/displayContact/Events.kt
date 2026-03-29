package de.benkralex.socius.ui.components.displayContact

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.benkralex.socius.data.model.Event
import de.benkralex.socius.data.model.Type
import de.benkralex.socius.data.settings.getFormattedDate

@Composable
fun EventsWidget(
    events: List<Event>
) {
    if (events.isEmpty()) {
        return
    }
    Card (
        modifier = Modifier
            .padding(bottom = 0.dp, top = 12.dp, end = 16.dp, start = 16.dp)
            .fillMaxWidth(),
    ) {
        Column {
            events.forEach { event ->
                Row {
                    if (event.type == Type.Event.BIRTHDAY) {
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
                        val formattedDate = getFormattedDate(event.day, event.month, event.year ?: 0)
                        Text(
                            text = formattedDate,
                        )
                        Text(
                            stringResource(Type.translateType(event.type)),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
                if (events.indexOf(event) != (events.size - 1)) HorizontalDivider(
                    thickness = 2.dp,
                )
            }
        }
    }
}