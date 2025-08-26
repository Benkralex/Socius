package de.benkralex.socius.widgets.contactInformation

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Phone
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
import de.benkralex.socius.data.PhoneNumber
import de.benkralex.socius.widgets.contactInformation.helpers.translateType

@Composable
fun PhoneNumbersWidget(
    phoneNumbers: List<PhoneNumber>
) {
    if (phoneNumbers.isEmpty()) {
        return
    }
    val context = LocalContext.current
    Card (
        modifier = Modifier
            .padding(bottom = 0.dp, top = 12.dp, end = 16.dp, start = 16.dp)
            .fillMaxWidth(),
    ) {
        Column {
            phoneNumbers.forEach { phoneNumber ->
                Row {
                    Icon(
                        imageVector = Icons.Outlined.Phone,
                        contentDescription = "Phone",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(8.dp)
                            .padding(start = 16.dp)
                            .clickable {
                                val intent = Intent(Intent.ACTION_DIAL)
                                    .apply {
                                    data = "tel:${phoneNumber.number}".toUri()
                                }
                                context.startActivity(intent)
                            },
                    )
                    Column (
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(8.dp),
                    ) {
                        Text(
                            text = phoneNumber.number,
                        )
                        Text(
                            text = translateType(phoneNumber.type, phoneNumber.label),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
                if (phoneNumbers.indexOf(phoneNumber) != (phoneNumbers.size - 1)) HorizontalDivider(
                    thickness = 2.dp,
                )
            }
        }
    }
}