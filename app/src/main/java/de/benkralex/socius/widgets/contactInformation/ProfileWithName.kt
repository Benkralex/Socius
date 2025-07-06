package de.benkralex.socius.widgets.contactInformation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import de.benkralex.socius.data.Contact
import de.benkralex.socius.data.settings.getFormattedName

@Composable
fun ProfileWithName(
    contact: Contact,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(16.dp)
    )
    {
        if (contact.photoBitmap != null) {
            Image(
                bitmap = contact.photoBitmap!!.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterVertically)
            )
        } else {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = "Profile Icon",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(30.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary
                    )
                    .padding(5.dp),
                tint = MaterialTheme.colorScheme.background
            )
        }
        Text(
            text = getFormattedName(contact),
            modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.CenterVertically)
                .fillMaxWidth(),
            style = MaterialTheme.typography.headlineSmall
        )
    }
}