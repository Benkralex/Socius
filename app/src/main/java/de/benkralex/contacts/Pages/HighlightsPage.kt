package de.benkralex.contacts.Pages

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import de.benkralex.contacts.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HighlightsPage() {
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.menu_bar_highlights))
                }
            )
        },
    ) { paddingValues ->
        Text(
            text = "Highlights content goes here",
            modifier = Modifier.padding(paddingValues)
        )
    }
}