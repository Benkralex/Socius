package de.benkralex.socius.widgets.contactInformation

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
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
import de.benkralex.socius.data.PostalAddress
import androidx.core.net.toUri
import de.benkralex.socius.widgets.contactInformation.helpers.translateType

@Composable
fun PostalAddressesWidget(
    postalAddresses: List<PostalAddress>
) {
    if (postalAddresses.isEmpty()) {
        return
    }
    val context = LocalContext.current
    Card (
        modifier = Modifier
            .padding(bottom = 0.dp, top = 12.dp, end = 16.dp, start = 16.dp)
            .fillMaxWidth(),
    ) {
        Column {
            postalAddresses.forEach { postalAddress ->
                Row {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "Address",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(8.dp)
                            .padding(start = 16.dp)
                            .clickable(
                                onClick = {
                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        "geo:0,0?q=${postalAddress.street},${postalAddress.city}".toUri()
                                    )
                                    context.startActivity(intent)
                                }
                            )
                    )
                    Column (
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(8.dp),
                    ) {
                        if (postalAddress.street != null) Text(
                            text = listOfNotNull(postalAddress.street).joinToString(" "),
                        )
                        if (postalAddress.postcode != null || postalAddress.city != null) Text(
                            text = listOfNotNull(postalAddress.postcode, postalAddress.city).joinToString(" "),
                        )
                        if (postalAddress.region != null || postalAddress.country != null) Text(
                            text = listOfNotNull(postalAddress.region, postalAddress.country).joinToString(" "),
                        )
                        Text(
                            text = translateType(postalAddress.type, postalAddress.label),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
                if (postalAddresses.indexOf(postalAddress) != (postalAddresses.size - 1)) HorizontalDivider(
                    thickness = 2.dp,
                )
            }
        }
    }
}