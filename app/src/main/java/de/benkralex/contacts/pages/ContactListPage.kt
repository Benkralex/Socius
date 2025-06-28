package de.benkralex.contacts.pages

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import de.benkralex.contacts.widgets.ContactsList
import de.benkralex.contacts.backend.Contact

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ContactListPage(
    contacts: List<Contact>? = null,
    menuBar: @Composable () -> Unit,
    onContactSelected: (Int) -> Unit = {}
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                Log.d("ContactsListPage", "Add new Contact clicked")
            }) {
                Icon(Icons.Filled.Add, "Add")
            }
        },
        bottomBar = {
            menuBar()
        }
    ) { paddingValues ->
        ContactsList(
            contacts = contacts ?: emptyList(),
            paddingValues = paddingValues,
            onContactSelected = onContactSelected
        )
    }
}