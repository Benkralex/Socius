package de.benkralex.socius.pages.newContact

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.benkralex.socius.R
import de.benkralex.socius.data.model.Contact
import de.benkralex.socius.data.model.ContactOrigin
import de.benkralex.socius.theme.DarkColorScheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NewContactPage(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    //openContact: (Int) -> Unit = {},
    viewModel: NewContactViewModel = viewModel<NewContactViewModel>(),
    contact: Contact = Contact(id = "new", ContactOrigin.LOCAL),
) {
    if (!viewModel.isInitialized) viewModel.loadFromContact(contact)
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val starIcon = if (viewModel.isStarred) Icons.Outlined.Star else Icons.Outlined.StarOutline
    val starDescription = "${stringResource(R.string.content_desc_toggle_starred)} (${
        if (viewModel.isStarred) {
            stringResource(R.string.starred)
        } else {
            stringResource(R.string.not_starred)
        }
    })"
    val saveRequested = remember { mutableStateOf(false) }
    LaunchedEffect(viewModel.isSaving, viewModel.error, saveRequested.value) {
        if (saveRequested.value && !viewModel.isSaving) {
            saveRequested.value = false
            if (!viewModel.error) {
                onBackClick()
            }
        }
    }
    var showCloseDialog by remember { mutableStateOf(false) }
    if (showCloseDialog) {
        BasicAlertDialog(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceBright,
                    shape = RoundedCornerShape(CornerSize(30.dp))
                )
                .padding(16.dp)
                .fillMaxWidth(),
            onDismissRequest = {
                showCloseDialog = false
            },
            content = {
                Column {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text =  stringResource(R.string.close_without_saving_dialog),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(Modifier.height(20.dp))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {
                            onBackClick()
                        },
                        colors = if (!isSystemInDarkTheme()) ButtonDefaults.buttonColors().copy(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError,
                        ) else ButtonDefaults.buttonColors().copy(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer,
                        ),
                    ) {
                        Text(
                            text = stringResource(R.string.close),
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {
                            showCloseDialog = false
                        },
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                        )
                    }
                }
            },
        )
    }
    BackHandler {
        if (viewModel.hasNoChanges()) {
            onBackClick()
        } else {
            showCloseDialog = true
        }
    }
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
                                onClick = {
                                    if (viewModel.hasNoChanges()) {
                                        onBackClick()
                                    } else {
                                        showCloseDialog = true
                                    }
                                }
                            )
                    )
                },
                actions = {
                    Row {
                        IconButton(
                            onClick = {
                                viewModel.isStarred = !viewModel.isStarred
                            }
                        ) {
                            Icon(
                                imageVector = starIcon,
                                contentDescription = starDescription,
                                modifier = Modifier
                                    .padding(8.dp),
                            )
                        }
                        IconButton(
                            onClick = {
                                saveRequested.value = true
                                viewModel.saveContact()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = stringResource(R.string.save),
                            )
                        }
                    }
                }
            )
        },
    ) { paddingValues ->
        Column (
            modifier = Modifier
                .padding(paddingValues)
                .padding(10.dp)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            EditStructuredName(viewModel = viewModel)
            AnimatedVisibility(viewModel.workInformationState.showFields) {
                EditWorkInformation(viewModel = viewModel)
            }
            AnimatedVisibility(viewModel.emailsState.showFields) {
                EditEmails(viewModel = viewModel)
            }
            AnimatedVisibility(viewModel.phoneNumbersState.showFields) {
                EditPhone(viewModel = viewModel)
            }
            AnimatedVisibility(viewModel.eventsState.showFields) {
                EditEvents(viewModel = viewModel)
            }
            AnimatedVisibility(viewModel.postalAddressesState.showFields) {
                EditPostalAddresses(viewModel = viewModel)
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
                    onClick = { viewModel.emailsState.addNew() },
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
                    onClick = { viewModel.phoneNumbersState.addNew() },
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
                    onClick = { viewModel.eventsState.addNew() },
                ) {
                    Row (
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .padding(vertical = 12.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Event,
                            contentDescription = null,
                        )
                        Text(stringResource(R.string.event))
                    }
                }
                Button(
                    modifier = Modifier
                        .weight(1f),
                    onClick = { viewModel.postalAddressesState.addNew() },
                ) {
                    Row (
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .padding(vertical = 12.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = null,
                        )
                        Text(stringResource(R.string.postalAddress))
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
                        keyboardController?.hide()
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