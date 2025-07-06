package de.benkralex.socius.widgets.contactInformation

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Language
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
import de.benkralex.socius.data.Website
import de.benkralex.socius.widgets.contactInformation.helpers.translateType

@Composable
fun WebsitesWidget(
    websites: List<Website>
) {
    if (websites.isEmpty()) {
        return
    }
    val context = LocalContext.current
    Card (
        modifier = Modifier
            .padding(bottom = 0.dp, top = 12.dp, end = 16.dp, start = 16.dp)
            .fillMaxWidth(),
    ) {
        Column {
            websites.forEach { website ->
                Row {
                    Icon(
                        imageVector = Icons.Outlined.Language,
                        contentDescription = "Website",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(8.dp)
                            .padding(start = 16.dp)
                            .clickable(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW).apply {
                                       data = website.url.toUri()
                                    }
                                    context.startActivity(intent)
                                }
                            )
                    )
                    Column (
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(8.dp),
                    ) {
                        Text(
                            text = website.url,
                        )
                        Text(
                            text = translateType(
                                website.type,
                                website.label ?: stringResource(R.string.type_homepage)
                            ),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
                if (websites.indexOf(website) != (websites.size - 1)) HorizontalDivider(
                    thickness = 2.dp,
                )
            }
        }
    }
}