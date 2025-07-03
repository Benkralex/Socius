package de.benkralex.contacts.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.DriveFileRenameOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.benkralex.contacts.R
import androidx.compose.runtime.mutableIntStateOf
import de.benkralex.contacts.widgets.settingsWidgets.DateFormattingWidget
import de.benkralex.contacts.widgets.settingsWidgets.NameFormattingWidget

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SettingsPage(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    Scaffold (
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.page_settings))
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier
                            .padding(5.dp)
                            .clickable(
                                onClick = onBackClick
                            )
                    )
                },
            )
        },
    ) { paddingValues ->
        var selectedTabIndex by remember { mutableIntStateOf(0) }
        val tabs = listOf(
            stringResource(R.string.settings_name_formatting),
            stringResource(R.string.settings_date_formatting),
        )
        val icons = listOf(
            Icons.Outlined.DriveFileRenameOutline,
            Icons.Outlined.DateRange,
        )
        Column (
            modifier = Modifier.padding(paddingValues)
        ) {
            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex,
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = icons[index],
                                contentDescription = title,
                            )
                        },
                    )
                }
            }
            Box(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                when (selectedTabIndex) {
                    0 -> NameFormattingWidget()
                    1 -> DateFormattingWidget()
                    else -> Text("Unknown Tab")
                }
            }
        }
    }
}