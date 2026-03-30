package de.benkralex.socius.ui.components.contactsList

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import de.benkralex.socius.R
import de.benkralex.socius.data.contacts.contacts
import de.benkralex.socius.ui.pages.ContactListPageViewModel
import de.benkralex.socius.ui.import_export.ExportDialog
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ContactsList(
    modifier: Modifier = Modifier,
    onContactSelected: (Int) -> Unit = {},
    viewModel: ContactListPageViewModel = viewModel<ContactListPageViewModel>(),
) {
    BackHandler(viewModel.selected.isNotEmpty()) {
        viewModel.deselectAll()
    }

    if (viewModel.showDeleteSelectedConfirmationDialog) {
        DeleteSelectedConfirmDialog(viewModel)
    }

    if (viewModel.showJumpToLetterDialog) {
        JumpToLetterDialog(viewModel)
    }

    if (viewModel.showExportTypeSelectionDialog) {
        ExportDialog(
            onDismiss = { viewModel.showExportTypeSelectionDialog = false },
            contacts = viewModel.selected.toList(),
        )
    }

    viewModel.pullToRefreshState = rememberPullToRefreshState()
    val listState = rememberLazyListState()
    val sectionStartIndexes = remember(viewModel.grouped) {
        buildMap {
            var currentIndex = 0
            viewModel.grouped.forEach { (initial, contactsForInitial) ->
                put(initial, currentIndex)
                currentIndex += 1 + contactsForInitial.size
            }
        }
    }
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
        LaunchedEffect(viewModel.scrollRequestToken, sectionStartIndexes) {
            val targetLetter = viewModel.pendingScrollTarget ?: return@LaunchedEffect
            val targetIndex = sectionStartIndexes[targetLetter] ?: run {
                viewModel.consumeScrollRequest()
                return@LaunchedEffect
            }

            listState.scrollToItem(targetIndex)
            viewModel.consumeScrollRequest()
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
            LazyColumn(state = listState) {
                //Contacts
                viewModel.grouped.forEach { (initial, contactsForInitial) ->
                    stickyHeader {
                        if (initial == "starred") {
                            ContactsListHeading(
                                imageVector = Icons.Filled.Star,
                                contentDescription = stringResource(R.string.starred),
                                modifier = Modifier
                                    .clickable {
                                        viewModel.showJumpToLetterDialog = true
                                    },
                            )
                        } else {
                            ContactsListHeading(
                                text = initial,
                                modifier = Modifier
                                    .clickable {
                                        viewModel.showJumpToLetterDialog = true
                                    },
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