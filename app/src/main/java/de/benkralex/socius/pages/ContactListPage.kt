package de.benkralex.socius.pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.benkralex.socius.widgets.ContactsList

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ContactListPage(
    modifier: Modifier = Modifier,
    menuBar: @Composable () -> Unit,
    onContactSelected: (Int) -> Unit = {},
    onNewContactCreate: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onNewContactCreate()
                },
            ) {
                Icon(Icons.Filled.Add, "Add")
            }
        },
        bottomBar = {
            menuBar()
        },
    ) { paddingValues ->
        ContactsList(
            modifier = Modifier
                .padding(paddingValues),
            onContactSelected = onContactSelected,
        )
    }
}