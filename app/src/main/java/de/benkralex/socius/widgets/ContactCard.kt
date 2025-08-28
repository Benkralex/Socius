package de.benkralex.socius.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.benkralex.socius.data.Contact
import de.benkralex.socius.data.settings.getFormattedName
import de.benkralex.socius.widgets.contactInformation.ProfilePicture

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ContactCard(
    modifier: Modifier = Modifier,
    contact: Contact,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0, 0, 0, 1),
        ),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            ProfilePicture(
                contact = contact,
                size = 40.dp,
                modifier = Modifier
                    .align(Alignment.CenterVertically),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = getFormattedName(contact),
                )
            }
        }
    }
}