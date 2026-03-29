package de.benkralex.socius.ui.components.editContact

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import de.benkralex.socius.R
import de.benkralex.socius.data.model.Type
import de.benkralex.socius.ui.pages.EditContactPageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPostalAddresses(
    modifier: Modifier = Modifier,
    viewModel: EditContactPageViewModel,
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
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = stringResource(R.string.postalAddress),
                modifier = Modifier
                    .padding(8.dp)
            )

            Column (
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 8.dp),
            ) {
                for (i in 0..<viewModel.addressesState.count) {
                    var streetFocused by remember { mutableStateOf(false) }
                    var houseNumberFocused by remember { mutableStateOf(false) }
                    var cityFocused by remember { mutableStateOf(false) }
                    var regionFocused by remember { mutableStateOf(false) }
                    var postcodeFocused by remember { mutableStateOf(false) }
                    var countryFocused by remember { mutableStateOf(false) }
                    var typeFocused by remember { mutableStateOf(false) }
                    var labelFocused by remember { mutableStateOf(false) }
                    val expanded by remember { derivedStateOf { streetFocused || houseNumberFocused || cityFocused || regionFocused || postcodeFocused || countryFocused || typeFocused || labelFocused } }

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged {
                                streetFocused = it.isFocused
                            },
                        value = viewModel.addressesState.streets[i].value,
                        onValueChange = { viewModel.addressesState.streets[i].value = it },
                        label = {
                            Text(
                                text = if (expanded)
                                    stringResource(R.string.postalAddress_street)
                                else
                                    stringResource(R.string.postalAddress_street) + " (" +
                                            stringResource(Type.translateType(viewModel.addressesState.types[i].value)) + ")"
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.addressesState.streets.removeAt(i)
                                    viewModel.addressesState.cities.removeAt(i)
                                    viewModel.addressesState.regions.removeAt(i)
                                    viewModel.addressesState.postcodes.removeAt(i)
                                    viewModel.addressesState.countries.removeAt(i)
                                    viewModel.addressesState.types.removeAt(i)
                                    viewModel.addressesState.count--
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
                        },
                    )
                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 3.dp)
                                .onFocusChanged { houseNumberFocused = it.isFocused },
                            value = viewModel.addressesState.houseNumbers[i].value,
                            onValueChange = {
                                if (!it.isDigitsOnly()) return@OutlinedTextField
                                viewModel.addressesState.houseNumbers[i].value = it
                            },
                            label = { Text(stringResource(R.string.postalAddress_house_number)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 3.dp)
                                .onFocusChanged { postcodeFocused = it.isFocused },
                            value = viewModel.addressesState.postcodes[i].value?.toString() ?: "",
                            onValueChange = {
                                if (!it.isDigitsOnly()) {
                                    return@OutlinedTextField
                                }
                                viewModel.addressesState.postcodes[i].value = it.toIntOrNull()
                            },
                            label = { Text(stringResource(R.string.postalAddress_postcode)) },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.NumberPassword
                            ),
                        )
                    }
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { cityFocused = it.isFocused },
                        value = viewModel.addressesState.cities[i].value,
                        onValueChange = { viewModel.addressesState.cities[i].value = it },
                        label = { Text(stringResource(R.string.postalAddress_city)) },
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { regionFocused = it.isFocused },
                        value = viewModel.addressesState.regions[i].value,
                        onValueChange = { viewModel.addressesState.regions[i].value = it },
                        label = { Text(stringResource(R.string.postalAddress_region)) },
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { countryFocused = it.isFocused },
                        value = viewModel.addressesState.countries[i].value,
                        onValueChange = { viewModel.addressesState.countries[i].value = it },
                        label = { Text(stringResource(R.string.postalAddress_country)) },
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
                                    value = stringResource(Type.translateType(viewModel.addressesState.types[i].value)),
                                    onValueChange = { },
                                    readOnly = true,
                                    label = { Text(stringResource(R.string.postalAddress_type)) },
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
                                    Type.Address.entries.forEach { selectedType ->
                                        DropdownMenuItem(
                                            text = { Text(stringResource(Type.translateType(selectedType))) },
                                            onClick = {
                                                viewModel.addressesState.types[i].value = selectedType
                                                dropdownExpanded = false
                                            },
                                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                        )
                                    }
                                }
                            }
                            AnimatedVisibility(viewModel.addressesState.types[i].value == Type.Address.CUSTOM) {
                                OutlinedTextField(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .onFocusChanged {
                                            labelFocused = it.isFocused
                                        },
                                    value = viewModel.addressesState.types[i].value.label ?: "",
                                    onValueChange = { viewModel.addressesState.types[i].value.label = it },
                                    label = { Text(stringResource(R.string.postalAddress_label)) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}