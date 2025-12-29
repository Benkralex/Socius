package de.benkralex.socius.pages.newContact

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.benkralex.socius.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditStructuredName(
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
                imageVector = Icons.Outlined.Person,
                contentDescription = stringResource(R.string.content_desc_name),
                modifier = Modifier
                    .padding(8.dp)
            )

            Column (
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 8.dp),
            ) {
                AnimatedVisibility(viewModel.structuredNameState.showPrefixTextField) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = viewModel.structuredNameState.prefix,
                        onValueChange = { viewModel.structuredNameState.prefix = it },
                        label = { Text(stringResource(R.string.name_prefix)) },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.structuredNameState.prefix = ""
                                    viewModel.structuredNameState.showPrefixTextField = false
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
                }
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = viewModel.structuredNameState.givenName,
                    onValueChange = { viewModel.structuredNameState.givenName = it },
                    label = { Text(stringResource(R.string.name_given_name)) },
                )
                AnimatedVisibility(viewModel.structuredNameState.showMiddleNameTextField) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = viewModel.structuredNameState.middleName,
                        onValueChange = { viewModel.structuredNameState.middleName = it },
                        label = { Text(stringResource(R.string.name_middle_name)) },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.structuredNameState.middleName = ""
                                    viewModel.structuredNameState.showMiddleNameTextField = false
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
                }
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = viewModel.structuredNameState.familyName,
                    onValueChange = { viewModel.structuredNameState.familyName = it },
                    label = { Text(stringResource(R.string.name_family_name)) },
                )
                AnimatedVisibility(viewModel.structuredNameState.showSuffixTextField) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = viewModel.structuredNameState.suffix,
                        onValueChange = { viewModel.structuredNameState.suffix = it },
                        label = { Text(stringResource(R.string.name_suffix)) },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.structuredNameState.suffix = ""
                                    viewModel.structuredNameState.showSuffixTextField = false
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
                }
                AnimatedVisibility(viewModel.structuredNameState.showNicknameTextField) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = viewModel.structuredNameState.nickname,
                        onValueChange = { viewModel.structuredNameState.nickname = it },
                        label = { Text(stringResource(R.string.name_nickname)) },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.structuredNameState.nickname = ""
                                    viewModel.structuredNameState.showNicknameTextField = false
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
                }
            }
        }
    }
}