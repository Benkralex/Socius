package de.benkralex.socius.widgets

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Flip
import androidx.compose.material.icons.outlined.ImportExport
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.SelectAll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.benkralex.socius.MainActivity
import de.benkralex.socius.R
import de.benkralex.socius.data.contacts.contacts
import de.benkralex.socius.data.settings.getFormattedName
import de.benkralex.socius.pages.ContactsListViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ContactsList(
    modifier: Modifier = Modifier,
    onContactSelected: (Int) -> Unit = {},
    viewModel: ContactsListViewModel = viewModel<ContactsListViewModel>(),
) {
    BackHandler(viewModel.selected.isNotEmpty()) {
        viewModel.deselectAll()
    }

    if (viewModel.showDeleteSelectedConfirmationDialog) {
        BasicAlertDialog(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceBright,
                    shape = RoundedCornerShape(CornerSize(30.dp))
                )
                .padding(16.dp)
                .fillMaxWidth(),
            onDismissRequest = {
                viewModel.showDeleteSelectedConfirmationDialog = false
            },
            content = {
                Column {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text =  if (viewModel.selected.size == 1)
                                stringResource(R.string.confirm_deletion).replace("%name%", getFormattedName(viewModel.selected.first()))
                            else
                                stringResource(R.string.delete_count_contacts).replace("%count%", viewModel.selected.size.toString()),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(Modifier.height(20.dp))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {
                            viewModel.showDeleteSelectedConfirmationDialog = false
                            viewModel.deleteSelected()
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
                            text = stringResource(R.string.delete),
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {
                            viewModel.showDeleteSelectedConfirmationDialog = false
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

    viewModel.pullToRefreshState = rememberPullToRefreshState()
    val hapticFeedback = LocalHapticFeedback.current
    Column (
        modifier = modifier
    ) {
        AnimatedVisibility(viewModel.displaySelectionActions) {
            SelectionActions(viewModel)
        }
        AnimatedVisibility(viewModel.displaySearch) {
            SearchBar(viewModel)
        }
        AnimatedVisibility(viewModel.displayLabels) {
            GroupFilter(viewModel)
        }
        LaunchedEffect(viewModel.willRefresh) {
            when {
                viewModel.willRefresh -> {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    delay(70)
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    delay(100)
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                }

                !viewModel.isRefreshing && (viewModel.pullToRefreshState?.distanceFraction ?: 0f) > 0f -> {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                }
            }
        }
        PullToRefreshBox(
            isRefreshing = viewModel.isRefreshing,
            onRefresh = { viewModel.refreshContacts() },
            modifier = Modifier.fillMaxSize(),
            state = viewModel.pullToRefreshState!!,
            indicator = {
                PullToRefreshDefaults.LoadingIndicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter),
                    state = viewModel.pullToRefreshState!!,
                    isRefreshing = viewModel.isRefreshing,
                )
            }
        ) {
            LazyColumn {
                //Contacts
                viewModel.grouped.forEach { (initial, contactsForInitial) ->
                    stickyHeader {
                        if (initial == "starred") {
                            ContactsListHeading(
                                imageVector = Icons.Filled.Star,
                                contentDescription = stringResource(R.string.starred)
                            )
                        } else {
                            ContactsListHeading(
                                text = initial
                            )
                        }
                    }
                    items(contactsForInitial) { c ->
                        ContactCard(
                            contact = c,
                            modifier = Modifier
                                .combinedClickable(
                                    onClick = {
                                        if (viewModel.selected.isEmpty())
                                            onContactSelected(contacts.indexOf(c))
                                        else
                                            viewModel.toggleSelection(c)
                                    },
                                    onLongClick = {
                                        if (viewModel.selected.isNotEmpty())
                                            onContactSelected(contacts.indexOf(c))
                                        else
                                            viewModel.toggleSelection(c)
                                    }
                                ),
                            isSelected = viewModel.selected.contains(c),
                            onProfilePictureClick = {
                                viewModel.toggleSelection(c)
                            }
                        )
                    }
                }
                //No results
                if (viewModel.showNoResultsMsg) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text = stringResource(R.string.no_results)
                            )
                        }
                    }
                }
                //No contacts
                if (viewModel.showNoContactsMsg) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text = stringResource(R.string.no_contacts)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionActions(
    viewModel: ContactsListViewModel,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(8.dp)
            .padding(horizontal = 12.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = {
                viewModel.deselectAll()
            }
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = stringResource(R.string.content_desc_deselect_all),
            )
        }
        Text(
            text = stringResource(R.string.selected_count).replace(
                "%count%",
                viewModel.selected.size.toString()
            ),
            modifier = Modifier
                .padding(top = 3.dp),
        )
        Spacer(Modifier.weight(1f))
        IconButton(
            onClick = {
                viewModel.selectAll()
            }
        ) {
            Icon(
                imageVector = Icons.Outlined.SelectAll,
                contentDescription = stringResource(R.string.content_desc_select_all),
            )
        }
        IconButton(
            onClick = {
                viewModel.reverseSelection()
            }
        ) {
            Icon(
                imageVector = Icons.Outlined.Flip,
                contentDescription = stringResource(R.string.content_desc_reverse_selection),
            )
        }
        IconButton(
            onClick = {
                viewModel.exportSelectedContacts(
                    context = MainActivity.instance,
                )
            }
        ) {
            Icon(
                imageVector = Icons.Outlined.ImportExport,
                contentDescription = null,
            )
        }
        Box {
            var showMenu by remember { mutableStateOf(false) }
            IconButton(
                onClick = {
                    showMenu = true
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = null,
                )
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                shape = RoundedCornerShape(8.dp),
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier
                    .width(200.dp),
            ) {
                DropdownMenuItem(
                    text = {
                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            )
                            Text(
                                text = stringResource(R.string.delete),
                                modifier = Modifier
                                    .padding(top = 3.dp),
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                            )
                        }
                    },
                    onClick = {
                        showMenu = false
                        viewModel.showDeleteSelectedConfirmationDialog = true
                    }
                )
            }
        }
    }
}

@Composable
fun SearchBar(
    viewModel: ContactsListViewModel,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = viewModel.searchQuery,
        onValueChange = { viewModel.onSearchQueryChange(it) },
        modifier = modifier
            .padding(8.dp)
            .padding(horizontal = 12.dp)
            .fillMaxWidth(),
        placeholder = {
            Text(
                text = stringResource(R.string.searchbar_placeholder),
            )
        },
        singleLine = true,
        leadingIcon = {
            Icon(
                modifier = Modifier
                    .padding(start = 8.dp),
                imageVector = Icons.Filled.Search,
                contentDescription = null,
            )
        },
        trailingIcon = {
            if (viewModel.searchQuery.isNotEmpty()) {
                Icon(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable { viewModel.onSearchQueryChange("") },
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(R.string.content_desc_clear_search)
                )
            }
        },
        shape = RoundedCornerShape(32.dp),
    )
}


@Composable
fun GroupFilter (
    viewModel: ContactsListViewModel,
    modifier: Modifier = Modifier,
) {
    LazyRow (
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        item {
            Spacer(Modifier.width(16.dp))
        }
        items(viewModel.selectedGroupsFilter.toList()) {
            FilterChip(
                selected = true,
                onClick = { viewModel.removeGroupFilter(it) },
                label = { Text(it) },
                modifier = Modifier
                    .padding(end = 8.dp),
                shape = RoundedCornerShape(32.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = stringResource(R.string.content_desc_selected),
                    )
                }
            )
        }
        items(viewModel.unselectedGroupsFilter.toList()) {
            FilterChip(
                selected = false,
                onClick = { viewModel.addGroupFilter(it) },
                label = { Text(it) },
                modifier = Modifier
                    .padding(end = 8.dp),
            )
        }
        item {
            Spacer(Modifier.width(16.dp))
        }
    }
}