package de.benkralex.socius.pages.newContact

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditWorkInformation(
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
                imageVector = Icons.Outlined.Work,
                contentDescription = "Work Information",
                modifier = Modifier
                    .padding(8.dp)
            )

            Column (
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 8.dp),
            ) {
                AnimatedVisibility(viewModel.showJobTitleTextField) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = viewModel.jobTitle,
                        onValueChange = { viewModel.jobTitle = it },
                        label = { Text("Job Titel") },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.jobTitle = ""
                                    viewModel.showJobTitleTextField = false
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.RemoveCircleOutline,
                                    contentDescription = "Remove Job Title",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier
                                        .padding(8.dp)
                                )
                            }
                        }
                    )
                }
                AnimatedVisibility(viewModel.showDepartmentTextField) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = viewModel.department,
                        onValueChange = { viewModel.department = it },
                        label = { Text("Abteilung") },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.department = ""
                                    viewModel.showDepartmentTextField = false
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.RemoveCircleOutline,
                                    contentDescription = "Remove Department",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier
                                        .padding(8.dp)
                                )
                            }
                        },
                    )
                }
                AnimatedVisibility(viewModel.showOrganizationTextField) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = viewModel.organization,
                        onValueChange = { viewModel.organization = it },
                        label = { Text("Organisation") },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.organization = ""
                                    viewModel.showOrganizationTextField = false
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.RemoveCircleOutline,
                                    contentDescription = "Remove Organization",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier
                                        .padding(8.dp)
                                )
                            }
                        },
                    )
                }
                AnimatedVisibility(
                    listOf(
                        viewModel.showJobTitleTextField,
                        viewModel.showDepartmentTextField,
                        viewModel.showOrganizationTextField,
                    ).any { !it }
                ) {
                    Button(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .fillMaxWidth(),
                        onClick = {
                            viewModel.showAddWorkInformationFieldBottomModal = true
                        }
                    ) {
                        Text("Feld hinzufügen")
                    }
                }
            }
        }
    }
    AnimatedVisibility(viewModel.showAddWorkInformationFieldBottomModal) {
        ModalBottomSheet(
            modifier = modifier,
            onDismissRequest = {
                viewModel.showAddWorkInformationFieldBottomModal = false
            }
        ) {
            if (!viewModel.showJobTitleTextField) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .clickable {
                            viewModel.showJobTitleTextField = true
                            viewModel.showAddWorkInformationFieldBottomModal = false
                        },
                    text = "Job Titel hinzufügen"
                )
            }
            if (!viewModel.showDepartmentTextField) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .clickable {
                            viewModel.showDepartmentTextField = true
                            viewModel.showAddWorkInformationFieldBottomModal = false
                        },
                    text = "Abteilung hinzufügen"
                )
            }
            if (!viewModel.showOrganizationTextField) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .clickable {
                            viewModel.showOrganizationTextField = true
                            viewModel.showAddWorkInformationFieldBottomModal = false
                        },
                    text = "Organisation hinzufügen"
                )
            }
        }
    }
}