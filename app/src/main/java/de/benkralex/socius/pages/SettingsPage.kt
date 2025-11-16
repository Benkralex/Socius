package de.benkralex.socius.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.FormatPaint
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.benkralex.socius.R
import de.benkralex.socius.data.contacts.reloadSystemContacts
import de.benkralex.socius.data.settings.loadAndroidSystemContacts
import de.benkralex.socius.data.settings.saveSettings
import de.benkralex.socius.widgets.settings.BooleanSetting
import de.benkralex.socius.widgets.settings.BooleanSettingState
import de.benkralex.socius.widgets.settings.DateFormattingWidget
import de.benkralex.socius.widgets.settings.NameFormattingWidget

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
        val tabsList = listOf(
            TabData(
                title = stringResource(R.string.settings_general),
                icon = Icons.Outlined.Settings,
                content = {
                    val context = LocalContext.current
                    BooleanSetting(
                        title = stringResource(R.string.settings_load_android_system_contacts),
                        state = BooleanSettingState(
                            onChangeCallback = {
                                loadAndroidSystemContacts = it
                                saveSettings(context)
                                if (it) reloadSystemContacts()
                            },
                            initialValue = loadAndroidSystemContacts,
                        ),
                    )
                },
            ),
            TabData(
                title = stringResource(R.string.settings_formatting),
                icon = Icons.Outlined.FormatPaint,
                content = {
                    Column(
                        modifier = Modifier
                            .scrollable(
                                state = rememberScrollState(),
                                orientation = Orientation.Vertical,
                            )
                    ) {
                        NameFormattingWidget()
                        HorizontalDivider(
                            modifier = Modifier
                                .padding(
                                    vertical = 12.dp,
                                )
                        )
                        DateFormattingWidget()
                    }
                },
            ),
        )
        val pagerState = rememberPagerState {
            tabsList.size
        }
        LaunchedEffect(selectedTabIndex) {
            pagerState.animateScrollToPage(selectedTabIndex)
        }
        LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
            if (!pagerState.isScrollInProgress) {
                selectedTabIndex = pagerState.currentPage
            }
        }
        Column (
            modifier = Modifier.padding(paddingValues)
        ) {
            ScrollableTabRow (
                selectedTabIndex = selectedTabIndex,
                divider = {},
            ) {
                tabsList.forEachIndexed { index, tab ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = tab.title,
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title,
                            )
                        },
                    )
                }
            }
            HorizontalDivider()
            HorizontalPager (
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.Top,
            ) { page ->
                Box (
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    tabsList[page].content()
                }
            }
        }
    }
}

data class TabData(
    val title: String,
    val icon: ImageVector,
    val content: @Composable () -> Unit
)