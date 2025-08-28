package de.benkralex.socius.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.benkralex.socius.R
import de.benkralex.socius.data.contacts.contacts
import de.benkralex.socius.pages.ContactsListViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ContactsList(
    modifier: Modifier = Modifier,
    onContactSelected: (Int) -> Unit = {},
    viewModel: ContactsListViewModel = viewModel<ContactsListViewModel>(),
) {
    Column (
        modifier = modifier
    ) {
        SearchBar(viewModel)
        GroupFilter(viewModel)
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
                        modifier = Modifier.clickable(
                            onClick = { onContactSelected(contacts.indexOf(c)) }
                        )
                    )
                }
            }
            //Loading
            if (viewModel.showLoadingIndicator) {
                item {
                    Column (
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        LoadingIndicator()
                    }
                }
            }
            //No results
            if (viewModel.showNoResultsMsg) {
                item {
                    Column (
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