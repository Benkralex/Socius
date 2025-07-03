package de.benkralex.contacts.pages

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.benkralex.contacts.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagePage(
    modifier: Modifier = Modifier,
    menuBar: @Composable () -> Unit
) {
    Scaffold (
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.page_manage))
                }
            )
        },
        bottomBar = {
            menuBar()
        }
    ) { paddingValues ->
        Text(
            text = "PLACEHOLDER: Manage Page",
            modifier = Modifier
                .padding(paddingValues)
                .padding(8.dp)
        )
    }
}