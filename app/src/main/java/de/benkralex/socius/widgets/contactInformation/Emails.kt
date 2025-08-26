package de.benkralex.socius.widgets.contactInformation

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import de.benkralex.socius.data.Email
import de.benkralex.socius.widgets.contactInformation.helpers.translateType

@Composable
fun EmailsWidget(
    emails: List<Email>
) {
    if (emails.isEmpty()) {
        return
    }
    val context = LocalContext.current
    Card (
        modifier = Modifier
            .padding(bottom = 0.dp, top = 12.dp, end = 16.dp, start = 16.dp)
            .fillMaxWidth(),
    ) {
        Column {
            emails.forEach { email ->
                Row {
                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = "mailto:${email.address}".toUri()
                            }
                            context.startActivity(intent)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Email,
                            contentDescription = "Email",
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
                        Text(
                            text = email.address,
                        )
                        Text(
                            text = translateType(email.type, email.label),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
                if (emails.indexOf(email) != (emails.size - 1)) HorizontalDivider(
                    thickness = 2.dp,
                )
            }
        }
    }
}