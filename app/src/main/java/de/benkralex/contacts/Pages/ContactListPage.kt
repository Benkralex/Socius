package de.benkralex.contacts.Pages

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import de.benkralex.contacts.contactListPageWidgets.ContactsList
import de.benkralex.contacts.backend.Contact
import de.benkralex.contacts.backend.getAndroidSystemContacts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ContactListPage(
    menuBar: @Composable () -> Unit
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
        val context = LocalContext.current
        var contacts by remember { mutableStateOf<List<Contact>?>(null) }

        LaunchedEffect(Unit) {
            withContext(Dispatchers.IO) {
                contacts = getAndroidSystemContacts(context = context)
            }
        }

        ContactsList(
            contacts = contacts ?: emptyList(),
            paddingValues = paddingValues
        )
    }
}