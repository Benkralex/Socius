package de.benkralex.socius.pages.newContact

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp

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
                contentDescription = "Person Icon",
                modifier = Modifier
                    .padding(8.dp)
            )

            Column (
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 8.dp),
            ) {
                AnimatedVisibility(
                    visible = viewModel.showPrefixTextField,
                    enter = newContactFormEnterTransition,
                    exit = newContactFormExitTransition,
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = viewModel.prefix,
                        onValueChange = { viewModel.prefix = it },
                        label = { Text("Präfix") },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.prefix = ""
                                    viewModel.showPrefixTextField = false
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.RemoveCircleOutline,
                                    contentDescription = "Remove Prefix",
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
                    label = { Text("Vorname") },
                )
                AnimatedVisibility(
                    visible = viewModel.showMiddleNameTextField,
                    enter = newContactFormEnterTransition,
                    exit = newContactFormExitTransition,
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = viewModel.middleName,
                        onValueChange = { viewModel.middleName = it },
                        label = { Text("Zweitname") },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.middleName = ""
                                    viewModel.showMiddleNameTextField = false
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.RemoveCircleOutline,
                                    contentDescription = "Remove Middle Name",
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
                    label = { Text("Nachname") },
                )
                AnimatedVisibility(
                    visible = viewModel.showSuffixTextField,
                    enter = newContactFormEnterTransition,
                    exit = newContactFormExitTransition,
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = viewModel.suffix,
                        onValueChange = { viewModel.suffix = it },
                        label = { Text("Suffix") },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.suffix = ""
                                    viewModel.showSuffixTextField = false
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.RemoveCircleOutline,
                                    contentDescription = "Remove Suffix",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier
                                        .padding(8.dp)
                                )
                            }
                        },
                    )
                }
                AnimatedVisibility(
                    visible = viewModel.showNicknameTextField,
                    enter = newContactFormEnterTransition,
                    exit = newContactFormExitTransition,
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = viewModel.nickname,
                        onValueChange = { viewModel.nickname = it },
                        label = { Text("Spitzname") },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.nickname = ""
                                    viewModel.showNicknameTextField = false
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.RemoveCircleOutline,
                                    contentDescription = "Remove Nickname",
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