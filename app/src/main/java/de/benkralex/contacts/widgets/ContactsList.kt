package de.benkralex.contacts.widgets

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import de.benkralex.contacts.R
import de.benkralex.contacts.backend.Contact
import kotlin.comparisons.compareBy

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ContactsList(
    contacts: List<Contact>,
    paddingValues: PaddingValues
) {
    val textFieldState = rememberTextFieldState()
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }

    val filteredContacts = remember(searchQuery, contacts) {
        if (searchQuery.isBlank()) contacts
        else contacts.filter {
            it.displayName?.contains(searchQuery, ignoreCase = true) == true
        }
    }

    val grouped = remember(filteredContacts) {
        filteredContacts.groupBy {
            it.displayName?.firstOrNull()?.uppercase() ?: "?"
        }.toSortedMap(compareBy { it })
    }

    Column(
        //modifier = Modifier.fillMaxSize()
    ) {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .semantics { traversalIndex = 0f },
            inputField = {
                SearchBarDefaults.InputField(
                    query = textFieldState.text.toString(),
                    onQueryChange = {
                        textFieldState.edit {
                            replace(0, length, it)
                        }
                        searchQuery = it
                    },
                    onSearch = {
                        searchQuery = textFieldState.text.toString()
                        expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = false },
                    placeholder = { Text("Search contacts...") },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Search,
                            "Search"
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            Icon(
                                Icons.Filled.Clear,
                                "Clear search",
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clickable {
                                        textFieldState.edit { replace(0, length, "") }
                                        searchQuery = ""
                                    }
                            )
                        } else {
                            Icon(
                                Icons.Filled.Settings,
                                "Settings",
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clickable {
                                        Log.d("ContactsList", "Settings clicked")
                                    }
                            )
                        }
                    }
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = false }
        ) {}
        LazyColumn(
            contentPadding = paddingValues,
        ) {
            grouped.forEach { (initial, contactsForInitial) ->
                stickyHeader {
                    ContactsListHeading(text = initial)
                }
                items(contactsForInitial) { c ->
                    ContactCard(
                        name = c.displayName ?: stringResource(R.string.contact_no_name),
                        profilePicture = c.photoBitmap,
                    )
                }
            }
        }
    }
}