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
                AnimatedVisibility(viewModel.showPrefixTextField) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = viewModel.prefix,
                        onValueChange = { viewModel.prefix = it },
                        label = { Text(stringResource(R.string.name_prefix)) },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.prefix = ""
                                    viewModel.showPrefixTextField = false
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
                    value = viewModel.givenName,
                    onValueChange = { viewModel.givenName = it },
                    label = { Text(stringResource(R.string.name_given_name)) },
                )
                AnimatedVisibility(viewModel.showMiddleNameTextField) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = viewModel.middleName,
                        onValueChange = { viewModel.middleName = it },
                        label = { Text(stringResource(R.string.name_middle_name)) },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.middleName = ""
                                    viewModel.showMiddleNameTextField = false
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
                    value = viewModel.familyName,
                    onValueChange = { viewModel.familyName = it },
                    label = { Text(stringResource(R.string.name_family_name)) },
                )
                AnimatedVisibility(viewModel.showSuffixTextField) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = viewModel.suffix,
                        onValueChange = { viewModel.suffix = it },
                        label = { Text(stringResource(R.string.name_suffix)) },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.suffix = ""
                                    viewModel.showSuffixTextField = false
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
                AnimatedVisibility(viewModel.showNicknameTextField) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = viewModel.nickname,
                        onValueChange = { viewModel.nickname = it },
                        label = { Text(stringResource(R.string.name_nickname)) },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.nickname = ""
                                    viewModel.showNicknameTextField = false
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