package de.benkralex.contacts.widgets.settingsWidgets

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.benkralex.contacts.R
import de.benkralex.contacts.backend.nameFormat
import de.benkralex.contacts.backend.saveSettings

@Composable
fun NameFormattingWidget() {
    var format by remember {
        if (nameFormat == "%givenName% %familyName%") {
            mutableStateOf(false)
        } else {
            mutableStateOf(true)
        }
    }
    val context = LocalContext.current

    Text(
        text = stringResource(R.string.settings_name_formatting),
        style = MaterialTheme.typography.titleLarge
    )
    Row (
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            text = stringResource(R.string.settings_date_format_last_first),
        )
        Box(
            modifier = Modifier.fillMaxWidth(0.8f)
        )
        Switch (
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = 8.dp),
            checked = format,
            onCheckedChange = {
                format = it
                changeSetting(
                    format = it,
                    context = context,
                )
            },
            thumbContent = if (format) {
                {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize),
                    )
                }
            } else {
                {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize),
                    )
                }
            }
        )
    }
    Row (
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            text = stringResource(R.string.settings_date_format_first_last),
        )
        Box(
            modifier = Modifier.fillMaxWidth(0.8f)
        )
        Switch (
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = 8.dp),
            checked = !format,
            onCheckedChange = {
                format = !it
                changeSetting(
                    format = !it,
                    context = context,
                )
            },
            thumbContent = if (!format) {
                {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize),
                    )
                }
            } else {
                {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize),
                    )
                }
            }
        )
    }
}

fun changeSetting(format: Boolean, context: Context) {
    nameFormat = if (format) {
        "%familyName%, %givenName%"
    } else {
        "%givenName% %familyName%"
    }
    saveSettings(context)
}