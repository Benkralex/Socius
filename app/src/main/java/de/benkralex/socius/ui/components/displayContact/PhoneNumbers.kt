package de.benkralex.socius.ui.components.displayContact

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import de.benkralex.socius.R
import de.benkralex.socius.data.model.Phone
import de.benkralex.socius.data.model.Type

@Composable
fun PhoneNumbersWidget(
    phoneNumbers: List<Phone>
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
            phoneNumbers.forEach { phone ->
                Row {
                    Icon(
                        imageVector = Icons.Outlined.Phone,
                        contentDescription = stringResource(R.string.phone),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(8.dp)
                            .padding(start = 16.dp)
                            .clickable {
                                val intent = Intent(Intent.ACTION_DIAL)
                                    .apply {
                                    data = "tel:${phone.value}".toUri()
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
                            text = phone.value,
                        )
                        Text(
                            stringResource(Type.translateType(phone.type)),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
                if (phoneNumbers.indexOf(phone) != (phoneNumbers.size - 1)) HorizontalDivider(
                    thickness = 2.dp,
                )
            }
        }
    }
}