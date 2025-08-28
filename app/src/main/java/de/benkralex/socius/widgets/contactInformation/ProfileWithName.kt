package de.benkralex.socius.widgets.contactInformation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import de.benkralex.socius.data.Contact
import de.benkralex.socius.data.settings.getFormattedName

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProfileWithName(
    contact: Contact,
    modifier: Modifier = Modifier,
    onProfileClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .padding(16.dp)
    )
    {
        ProfilePicture(
            contact = contact,
            size = 40.dp,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .clickable { onProfileClick() }
        )
        Text(
            text = getFormattedName(contact),
            modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.CenterVertically)
                .fillMaxWidth(),
            style = MaterialTheme.typography.headlineSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}