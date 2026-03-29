package de.benkralex.socius.ui.components.displayContact

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.benkralex.socius.data.model.Address
import androidx.core.net.toUri
import de.benkralex.socius.R
import de.benkralex.socius.data.model.Type

@Composable
fun PostalAddressesWidget(
    addresses: List<Address>
) {
    if (addresses.isEmpty()) {
        return
    }
    val context = LocalContext.current
    Card (
        modifier = Modifier
            .padding(bottom = 0.dp, top = 12.dp, end = 16.dp, start = 16.dp)
            .fillMaxWidth(),
    ) {
        Column {
            addresses.forEach { address ->
                Row {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = stringResource(R.string.postalAddress),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(8.dp)
                            .padding(start = 16.dp)
                            .clickable {
                                if (address.street != null || address.city != null) {
                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        "geo:0,0?q=${
                                            listOfNotNull(
                                                address.street,
                                                if (address.street == null) null else address.streetNumber,
                                            ).joinToString(" ")
                                        },${address.city}".toUri()
                                    )
                                    context.startActivity(intent)
                                }
                            },
                    )
                    Column (
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(8.dp),
                    ) {
                        Text(
                            text = address.format(),
                        )
                        Text(
                            text = stringResource(Type.translateType(address.type)),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
                if (addresses.indexOf(address) != (addresses.size - 1)) HorizontalDivider(
                    thickness = 2.dp,
                )
            }
        }
    }
}