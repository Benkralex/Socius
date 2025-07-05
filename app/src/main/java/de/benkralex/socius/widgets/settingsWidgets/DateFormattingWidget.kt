package de.benkralex.socius.widgets.settingsWidgets

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.benkralex.socius.R
import de.benkralex.socius.backend.settings.dateFormat
import de.benkralex.socius.backend.settings.dateFormats
import de.benkralex.socius.backend.settings.saveSettings

@Composable
fun DateFormattingWidget(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var selectedFormat by remember { mutableIntStateOf(dateFormat) }

    Column (
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
        Text(
            text = stringResource(R.string.settings_date_formatting),
            style = MaterialTheme.typography.titleLarge
        )
        Column {
            for (f in dateFormats) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = {
                                selectedFormat = f.id
                                changeSetting(
                                    format = f.id,
                                    context = context,
                                )
                            }
                        )
                ) {
                    RadioButton(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(horizontal = 4.dp),
                        selected = (selectedFormat == f.id),
                        onClick = {
                            selectedFormat = f.id
                            changeSetting(
                                format = f.id,
                                context = context,
                            )
                        },
                    )
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterVertically),
                        text = stringResource(f.displayNameResource),
                    )
                }
            }
        }
    }
}

private fun changeSetting(format: Int, context: Context) {
    dateFormat = format
    saveSettings(context)
}