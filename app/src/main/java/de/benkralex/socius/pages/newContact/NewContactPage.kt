package de.benkralex.socius.pages.newContact

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.benkralex.socius.R
import de.benkralex.socius.theme.DarkColorScheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NewContactPage(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: NewContactViewModel = viewModel<NewContactViewModel>(),
) {
    Scaffold (
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.page_new_contact))
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.content_desc_back),
                        modifier = Modifier
                            .padding(5.dp)
                            .clickable(
                                onClick = onBackClick
                            )
                    )
                },
            )
        },
        floatingActionButton = {
            MediumFloatingActionButton(
                onClick = {
                    viewModel.saveContact()
                    onBackClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = stringResource(R.string.save),
                )
            }
        }
    ) { paddingValues ->
        Column (
            modifier = Modifier
                .padding(paddingValues)
                .padding(10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            EditStructuredName(viewModel = viewModel)
            AnimatedVisibility(viewModel.showWorkInformation) {
                EditWorkInformation(viewModel = viewModel)
            }
            AnimatedVisibility(viewModel.showEmailFields) {
                EditEmails(viewModel = viewModel)
            }
            AnimatedVisibility(viewModel.showPhoneFields) {
                EditPhone(viewModel = viewModel)
            }
            Row (
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
            ) {
                Button(
                    modifier = Modifier
                        .weight(1f),
                    onClick = {
                        viewModel.emailAddresses.add(mutableStateOf(""))
                        viewModel.emailTypes.add(mutableStateOf("home"))
                        viewModel.emailLabels.add(mutableStateOf(""))
                        viewModel.emailCount++
                    },
                ) {
                    Row (
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .padding(vertical = 12.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Mail,
                            contentDescription = null,
                        )
                        Text(stringResource(R.string.email))
                    }
                }
                Button(
                    modifier = Modifier
                        .weight(1f),
                    onClick = {
                        viewModel.phoneNumbers.add(mutableStateOf(""))
                        viewModel.phoneTypes.add(mutableStateOf("home"))
                        viewModel.phoneLabels.add(mutableStateOf(""))
                        viewModel.phoneCount++
                    },
                ) {
                    Row (
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .padding(vertical = 12.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Phone,
                            contentDescription = null,
                        )
                        Text(stringResource(R.string.phone))
                    }
                }
            }
            Row (
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
            ) {
                Button(
                    modifier = Modifier
                        .weight(1f),
                    onClick = {
                        viewModel.showAddFieldBottomModal = true
                    },
                ) {
                    Row (
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .padding(vertical = 12.dp),
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ListAlt,
                            contentDescription = null,
                        )
                        Text(stringResource(R.string.other))
                    }
                }
            }
            if (viewModel.showAddFieldBottomModal) {
                AddFieldBottomModal(viewModel = viewModel)
            }
        }
    }
}

@Preview
@Composable
private fun NewContactPagePreview() {
    MaterialTheme (
        colorScheme = DarkColorScheme,
    ) {
        NewContactPage()
    }
}