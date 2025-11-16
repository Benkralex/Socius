package de.benkralex.socius.pages.newContact

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.benkralex.socius.R
import de.benkralex.socius.widgets.contactInformation.helpers.translateType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEmails(
    modifier: Modifier = Modifier,
    viewModel: NewContactViewModel,
) {
    Card (
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Row (
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.Mail,
                contentDescription = stringResource(R.string.email),
                modifier = Modifier
                    .padding(8.dp)
            )

            Column (
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 8.dp),
            ) {
                for (i in 0 .. (viewModel.emailCount-1)) {
                    var addressFocused by remember { mutableStateOf(false) }
                    var typeFocused by remember { mutableStateOf(false) }
                    var labelFocused by remember { mutableStateOf(false) }
                    val expanded by remember { derivedStateOf { addressFocused || typeFocused || labelFocused } }
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged {
                                addressFocused = it.isFocused
                            },
                        value = viewModel.emailAddresses[i].value,
                        onValueChange = { viewModel.emailAddresses[i].value = it },
                        label = {
                            Text(
                                text = if (expanded)
                                    stringResource(R.string.email)
                                else
                                    stringResource(R.string.email) + " (" +
                                        translateType(
                                            type = viewModel.emailTypes[i].value,
                                            label = viewModel.emailLabels[i].value.ifBlank { null }
                                        ) + ")"
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.emailAddresses.removeAt(i)
                                    viewModel.emailTypes.removeAt(i)
                                    viewModel.emailLabels.removeAt(i)
                                    viewModel.emailCount--
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.RemoveCircleOutline,
                                    contentDescription = stringResource(R.string.remove),
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier
                                        .padding(8.dp)
                                )
                            }
                        }
                    )
                    AnimatedVisibility(expanded) {
                        Column {
                            var dropdownExpanded by remember { mutableStateOf(false) }
                            ExposedDropdownMenuBox(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                expanded = dropdownExpanded,
                                onExpandedChange = {dropdownExpanded = it}
                            ) {
                                OutlinedTextField(
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth()
                                        .onFocusChanged {
                                            typeFocused = it.isFocused
                                        },
                                    value = translateType(viewModel.emailTypes[i].value),
                                    onValueChange = { },
                                    readOnly = true,
                                    label = { Text(stringResource(R.string.email_type)) },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = dropdownExpanded
                                        )
                                    },
                                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                                )
                                ExposedDropdownMenu(
                                    expanded = dropdownExpanded,
                                    onDismissRequest = {
                                        dropdownExpanded = false
                                    }
                                ) {
                                    listOf("home", "work", "other", "custom").forEach { o ->
                                        DropdownMenuItem(
                                            text = { Text(translateType(o)) },
                                            onClick = {
                                                viewModel.emailTypes[i].value = o
                                                dropdownExpanded = false
                                            },
                                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                        )
                                    }
                                }
                            }
                            AnimatedVisibility(viewModel.emailTypes[i].value == "custom") {
                                OutlinedTextField(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .onFocusChanged {
                                            labelFocused = it.isFocused
                                        },
                                    value = viewModel.emailLabels[i].value,
                                    onValueChange = { viewModel.emailLabels[i].value = it },
                                    label = { Text(stringResource(R.string.email_label)) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}