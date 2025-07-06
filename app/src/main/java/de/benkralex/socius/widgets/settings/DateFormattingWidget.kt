package de.benkralex.socius.widgets.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.benkralex.socius.R
import de.benkralex.socius.data.settings.dateFormats

@Composable
fun DateFormattingWidget(
    modifier: Modifier = Modifier,
) {
    val state = DateFormattingState(LocalContext.current)
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
                                state.changeSetting(
                                    format = f.id,
                                )
                            }
                        )
                ) {
                    RadioButton(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(horizontal = 4.dp),
                        selected = (state.selectedFormat == f.id),
                        onClick = {
                            state.changeSetting(
                                format = f.id,
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