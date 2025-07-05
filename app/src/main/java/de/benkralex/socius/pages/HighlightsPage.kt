package de.benkralex.socius.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.benkralex.socius.R
import de.benkralex.socius.backend.Contact
import de.benkralex.socius.backend.settings.getFormattedDate
import de.benkralex.socius.backend.settings.getFormattedName
import de.benkralex.socius.backend.systemContacts
import de.benkralex.socius.widgets.contactInfromationWidgets.translateType
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HighlightsPage(
    modifier: Modifier = Modifier,
    menuBar: @Composable () -> Unit
) {
    Scaffold (
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.page_highlights))
                }
            )
        },
        bottomBar = {
            menuBar()
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            val events = getEventWidgets()
            if (events.isEmpty()) {
                Text(
                    text = stringResource(R.string.highlights_no_events_today),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                Column {
                    Text(
                        text = stringResource(R.string.highlights_events_today),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                    for (event in events) {
                        event()
                    }
                }
            }
        }
    }
}

fun getEventWidgets(): ArrayList<@Composable () -> Unit> {
    val events: ArrayList<@Composable () -> Unit> = ArrayList<@Composable () -> Unit>()
    for (c: Contact in systemContacts) {
        for (e in c.events) {
            if (e.day == null) continue
            if (e.month == null) continue
            if (e.day == LocalDate.now().dayOfMonth && e.month == LocalDate.now().monthValue) {
                events.add {
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                    ) {
                        Text(
                            text = translateType(
                                e.type,
                                e.label
                            ) + " " + getFormattedName(c),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .padding(8.dp)
                                .padding(horizontal = 12.dp)
                                .padding(top = 12.dp),
                        )
                        Text(
                            text = getFormattedDate(e.day!!, e.month!!, e.year ?: 0),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .padding(8.dp)
                                .padding(horizontal = 12.dp)
                                .padding(bottom = 12.dp),
                        )
                    }
                }
            }
        }
    }
    return events
}