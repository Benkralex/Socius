package de.benkralex.contacts.Pages

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SettingsPage() {
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text("Settings")
                }
            )
        },
    ) { paddingValues ->
        Text(
            text = "Settings content goes here",
            modifier = Modifier.padding(paddingValues)
        )
    }
}