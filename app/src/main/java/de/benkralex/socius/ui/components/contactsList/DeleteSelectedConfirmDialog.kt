package de.benkralex.socius.ui.components.contactsList

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import de.benkralex.socius.R
import de.benkralex.socius.data.settings.getFormattedName
import de.benkralex.socius.ui.pages.ContactListPageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteSelectedConfirmDialog(
    viewModel: ContactListPageViewModel,
    modifier: Modifier = Modifier,
) {
    BasicAlertDialog(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceBright,
                shape = RoundedCornerShape(CornerSize(30.dp))
            )
            .padding(16.dp)
            .fillMaxWidth(),
        onDismissRequest = {
            viewModel.showDeleteSelectedConfirmationDialog = false
        },
        content = {
            Column {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text =  if (viewModel.selected.size == 1)
                        stringResource(R.string.confirm_deletion).replace("%name%", getFormattedName(viewModel.selected.first()))
                    else
                        stringResource(R.string.delete_count_contacts).replace("%count%", viewModel.selected.size.toString()),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(Modifier.height(20.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        viewModel.showDeleteSelectedConfirmationDialog = false
                        viewModel.deleteSelected()
                    },
                    colors = if (!isSystemInDarkTheme()) ButtonDefaults.buttonColors().copy(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError,
                    ) else ButtonDefaults.buttonColors().copy(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    ),
                ) {
                    Text(
                        text = stringResource(R.string.delete),
                    )
                }
                Spacer(Modifier.height(8.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        viewModel.showDeleteSelectedConfirmationDialog = false
                    },
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                    )
                }
            }
        },
    )
}