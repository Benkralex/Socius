package de.benkralex.socius.pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.benkralex.socius.widgets.ContactsList
import de.benkralex.socius.data.Contact

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ContactListPage(
    modifier: Modifier = Modifier,
    contacts: MutableList<Contact>,
    menuBar: @Composable () -> Unit,
    onContactSelected: (Int) -> Unit = {},
    onSettingsSelected: () -> Unit = {},
    onNewContactCreate: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onNewContactCreate()
            }) {
                Icon(Icons.Filled.Add, "Add")
            }
        },
        bottomBar = {
            menuBar()
        }
    ) { paddingValues ->
        ContactsList(
            contacts = contacts,
            paddingValues = paddingValues,
            onContactSelected = onContactSelected,
            onSettingsSelected = onSettingsSelected
        )
    }
}