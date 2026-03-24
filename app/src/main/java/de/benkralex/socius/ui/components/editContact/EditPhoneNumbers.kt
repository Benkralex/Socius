package de.benkralex.socius.ui.components.editContact

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Phone
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
import de.benkralex.socius.ui.components.displayContact.helpers.translateType
import de.benkralex.socius.ui.pages.NewContactPageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPhone(
    modifier: Modifier = Modifier,
    viewModel: NewContactPageViewModel,
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
                imageVector = Icons.Outlined.Phone,
                contentDescription = stringResource(R.string.phone),
                modifier = Modifier
                    .padding(8.dp)
            )

            Column (
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 8.dp),
            ) {
                for (i in 0..<viewModel.phoneNumbersState.count) {
                    var numberFocused by remember { mutableStateOf(false) }
                    var typeFocused by remember { mutableStateOf(false) }
                    var labelFocused by remember { mutableStateOf(false) }
                    val expanded by remember { derivedStateOf { numberFocused || typeFocused || labelFocused } }
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged {
                                numberFocused = it.isFocused
                            },
                        value = viewModel.phoneNumbersState.numbers[i].value,
                        onValueChange = { viewModel.phoneNumbersState.numbers[i].value = it },
                        label = {
                            Text(
                                text = if (expanded)
                                    stringResource(R.string.phone)
                                else
                                    stringResource(R.string.phone) + " (" +
                                            translateType(
                                                type = viewModel.phoneNumbersState.types[i].value,
                                                label = viewModel.phoneNumbersState.labels[i].value.ifBlank { null }
                                            ) + ")"
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.phoneNumbersState.numbers.removeAt(i)
                                    viewModel.phoneNumbersState.types.removeAt(i)
                                    viewModel.phoneNumbersState.labels.removeAt(i)
                                    viewModel.phoneNumbersState.count--
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
                                    value = translateType(viewModel.phoneNumbersState.types[i].value),
                                    onValueChange = { },
                                    readOnly = true,
                                    label = { Text(stringResource(R.string.phone_type)) },
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
                                    listOf("home", "mobile", "work_mobile", "work", "fax_home", "fax_work", "pager", "work_pager", "other", "custom").forEach { o ->
                                        DropdownMenuItem(
                                            text = { Text(translateType(o)) },
                                            onClick = {
                                                viewModel.phoneNumbersState.types[i].value = o
                                                dropdownExpanded = false
                                            },
                                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                        )
                                    }
                                }
                            }
                            AnimatedVisibility(viewModel.phoneNumbersState.types[i].value == "custom") {
                                OutlinedTextField(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .onFocusChanged {
                                            labelFocused = it.isFocused
                                        },
                                    value = viewModel.phoneNumbersState.labels[i].value,
                                    onValueChange = { viewModel.phoneNumbersState.labels[i].value = it },
                                    label = { Text(stringResource(R.string.phone_label)) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}