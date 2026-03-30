package de.benkralex.socius.ui.components.contactsList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Flip
import androidx.compose.material.icons.outlined.ImportExport
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.SelectAll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.benkralex.socius.R
import de.benkralex.socius.ui.pages.ContactListPageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionActions(
    viewModel: ContactListPageViewModel,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(8.dp)
            .padding(horizontal = 12.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = {
                viewModel.deselectAll()
            }
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = stringResource(R.string.content_desc_deselect_all),
            )
        }
        Text(
            text = stringResource(R.string.selected_count).replace(
                "%count%",
                viewModel.selected.size.toString()
            ),
            modifier = Modifier
                .padding(top = 3.dp),
        )
        Spacer(Modifier.weight(1f))
        IconButton(
            onClick = {
                viewModel.selectAll()
            }
        ) {
            Icon(
                imageVector = Icons.Outlined.SelectAll,
                contentDescription = stringResource(R.string.content_desc_select_all),
            )
        }
        IconButton(
            onClick = {
                viewModel.reverseSelection()
            }
        ) {
            Icon(
                imageVector = Icons.Outlined.Flip,
                contentDescription = stringResource(R.string.content_desc_reverse_selection),
            )
        }
        IconButton(
            onClick = {
                viewModel.showExportTypeSelectionDialog = true
            }
        ) {
            Icon(
                imageVector = Icons.Outlined.ImportExport,
                contentDescription = stringResource(R.string.export_contacts),
            )
        }
        Box {
            var showMenu by remember { mutableStateOf(false) }
            IconButton(
                onClick = {
                    showMenu = true
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = null,
                )
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                shape = RoundedCornerShape(8.dp),
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier
                    .width(200.dp),
            ) {
                DropdownMenuItem(
                    text = {
                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            )
                            Text(
                                text = stringResource(R.string.delete),
                                modifier = Modifier
                                    .padding(top = 3.dp),
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                            )
                        }
                    },
                    onClick = {
                        showMenu = false
                        viewModel.showDeleteSelectedConfirmationDialog = true
                    }
                )
            }
        }
    }
}