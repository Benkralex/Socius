package de.benkralex.socius.ui.components.displayContact

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.benkralex.socius.data.model.Contact

@Composable
fun WorkInformationWidget(
    contact: Contact
) {
    val smallInfos = listOfNotNull(
        contact.job.jobTitle,
        contact.job.department,
        contact.job.organization,
    )
    if (smallInfos.isEmpty()) {
        return
    }
    Card (
        modifier = Modifier
            .padding(bottom = 0.dp, top = 12.dp, end = 16.dp, start = 16.dp)
            .fillMaxWidth()
            .defaultMinSize(minHeight = 55.dp),
    ) {
        var text = ""
        smallInfos.forEach { s ->
            if (text.isNotEmpty()) {
                text += " • "
            }
            text += s
        }
        Row (
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp)
                .align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.Work,
                contentDescription = "Work Information",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(8.dp)
                    .padding(start = 16.dp),
            )
            Text(
                text = text,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(8.dp),
            )
        }
    }
}