package de.benkralex.socius.ui.components.editContact

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.window.DialogProperties
import de.benkralex.socius.R
import de.benkralex.socius.data.settings.dateFormat
import de.benkralex.socius.data.settings.dateFormats
import de.benkralex.socius.data.settings.getFormattedDate
import de.benkralex.socius.ui.components.displayContact.helpers.translateType
import de.benkralex.socius.ui.pages.NewContactPageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEvents(
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
                imageVector = Icons.Outlined.Event,
                contentDescription = stringResource(R.string.event),
                modifier = Modifier
                    .padding(8.dp)
            )

            Column (
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 8.dp),
            ) {
                for (i in 0..<viewModel.eventsState.count) {
                    var dateFocused by remember { mutableStateOf(false) }
                    var typeFocused by remember { mutableStateOf(false) }
                    var labelFocused by remember { mutableStateOf(false) }
                    val expanded by remember { derivedStateOf { dateFocused || typeFocused || labelFocused } }
                    var showDatePicker by remember { mutableStateOf(false) }
                    val datePickerState = rememberDatePickerState()
                    AnimatedVisibility (showDatePicker) {
                        BasicAlertDialog(
                            onDismissRequest = { showDatePicker = false },
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceBright,
                                    shape = RoundedCornerShape(CornerSize(30.dp))
                                )
                                .padding(16.dp)
                                .fillMaxWidth(0.95f),
                            properties = DialogProperties(
                                usePlatformDefaultWidth = false,
                            )
                        ) {
                            Column {
                                DatePicker(
                                    state = datePickerState,
                                    showModeToggle = true,
                                    colors = DatePickerDefaults.colors().copy(
                                        containerColor = MaterialTheme.colorScheme.surfaceBright,
                                    ),
                                )
                                Spacer(Modifier.height(4.dp))
                                OutlinedButton(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    onClick = {
                                        datePickerState.selectedDateMillis = viewModel.eventsState.dates[i].value
                                        showDatePicker = false
                                    },
                                ) {
                                    Text(
                                        text = stringResource(R.string.cancel),
                                    )
                                }
                                Spacer(Modifier.height(8.dp))
                                Button(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    onClick = {
                                        viewModel.eventsState.dates[i].value = datePickerState.selectedDateMillis
                                        showDatePicker = false
                                    },
                                ) {
                                    Text(
                                        text = stringResource(R.string.save),
                                    )
                                }
                            }
                        }
                    }
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged {
                                dateFocused = it.isFocused
                            },
                        value = viewModel.eventsState.dates[i].value?.let { getFormattedDate(it) } ?: "",
                        interactionSource = remember { MutableInteractionSource() }
                            .also { interactionSource ->
                                LaunchedEffect(interactionSource) {
                                    interactionSource.interactions.collect {
                                        if (it is PressInteraction.Release) {
                                            showDatePicker = true
                                        }
                                    }
                                }
                            },
                        readOnly = true,
                        onValueChange = { },
                        label = {
                            Text(
                                text = if (expanded)
                                    stringResource(R.string.event)
                                else
                                    stringResource(R.string.event) + " (" +
                                        translateType(
                                            type = viewModel.eventsState.types[i].value,
                                            label = viewModel.eventsState.labels[i].value.ifBlank { null }
                                        ) + ")"
                            )
                        },
                        placeholder = {
                            Text(
                                text = stringResource(dateFormats.first { it.id == dateFormat }.displayNameResource)
                            )
                        },
                        trailingIcon = {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                IconButton(
                                    onClick = {
                                        showDatePicker = true
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.DateRange,
                                        contentDescription = stringResource(R.string.pick_date),
                                        modifier = Modifier
                                            .padding(8.dp)
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        viewModel.eventsState.dates.removeAt(i)
                                        viewModel.eventsState.types.removeAt(i)
                                        viewModel.eventsState.labels.removeAt(i)
                                        viewModel.eventsState.count--
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
                        },
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
                                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                        .fillMaxWidth()
                                        .onFocusChanged {
                                            typeFocused = it.isFocused
                                        },
                                    value = translateType(viewModel.eventsState.types[i].value),
                                    onValueChange = { },
                                    readOnly = true,
                                    label = { Text(stringResource(R.string.event_type)) },
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
                                    listOf("birthday", "anniversary", "other", "custom").forEach { o ->
                                        DropdownMenuItem(
                                            text = { Text(translateType(o)) },
                                            onClick = {
                                                viewModel.eventsState.types[i].value = o
                                                dropdownExpanded = false
                                            },
                                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                        )
                                    }
                                }
                            }
                            AnimatedVisibility(viewModel.eventsState.types[i].value == "custom") {
                                OutlinedTextField(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .onFocusChanged {
                                            labelFocused = it.isFocused
                                        },
                                    value = viewModel.eventsState.labels[i].value,
                                    onValueChange = { viewModel.eventsState.labels[i].value = it },
                                    label = { Text(stringResource(R.string.event_label)) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}