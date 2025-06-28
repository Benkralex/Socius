package de.benkralex.contacts.Pages

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import de.benkralex.contacts.contactListPageWidgets.ContactsList
import de.benkralex.contacts.CustomNavigationBar
import de.benkralex.contacts.R
import de.benkralex.contacts.backend.Contact
import de.benkralex.contacts.backend.getAndroidSystemContacts

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ContactsListPage() {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                Log.d("ContactsListPage", "Add new Contact clicked")
            }) {
                Icon(Icons.Filled.Add, "Add")
            }
        },
        bottomBar = {
            CustomNavigationBar (
                items = listOf(
                    stringResource(R.string.menu_bar_contacts),
                    stringResource(R.string.menu_bar_highlights),
                    stringResource(R.string.menu_bar_manage)
                ),
                selectedIcons = listOf(
                    Icons.Filled.Person,
                    Icons.Filled.Favorite,
                    Icons.Filled.Build
                ),
                unselectedIcons = listOf(
                    Icons.Outlined.Person,
                    Icons.Outlined.FavoriteBorder,
                    Icons.Outlined.Build
                ),
            )
        }
    ) { paddingValues ->
        val context = LocalContext.current
        var contacts by remember { mutableStateOf<List<Contact>>(listOf()) }
        val scope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            contacts = getAndroidSystemContacts(context = context)
        }

        ContactsList(
            contacts = contacts,
            paddingValues = paddingValues
        )
    }
}