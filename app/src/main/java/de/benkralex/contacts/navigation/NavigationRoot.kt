package de.benkralex.contacts.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import de.benkralex.contacts.widgets.CustomNavigationBar
import de.benkralex.contacts.pages.ContactListPage
import de.benkralex.contacts.pages.HighlightsPage
import de.benkralex.contacts.pages.ManagePage
import de.benkralex.contacts.pages.SettingsPage
import de.benkralex.contacts.R
import kotlinx.serialization.Serializable

@Serializable
data object ContactListPageNavKey: NavKey
@Serializable
data object HighlightsPageNavKey: NavKey
@Serializable
data object ManagePageNavKey: NavKey
@Serializable
data object SettingsPageNavKey: NavKey
@Serializable
data class ContactDetailPageNavKey(val contactId: String): NavKey


@Composable
fun NavigationRoot(
    modifier: Modifier
) {
    val backStack = rememberNavBackStack(ContactListPageNavKey)
    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
            rememberSceneSetupNavEntryDecorator()
        ),
        entryProvider = { key ->
            when(key) {
                is ContactListPageNavKey -> {
                    NavEntry(
                        key = key
                    ) {
                        ContactListPage(
                            {
                                CustomNavigationBar(
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
                                    onClick = { index ->
                                        when (index) {
                                            0 -> backStack.add(ContactListPageNavKey)
                                            1 -> backStack.add(HighlightsPageNavKey)
                                            2 -> backStack.add(ManagePageNavKey)
                                        }
                                    },
                                    selectedIndex = 0
                                )
                            }
                        )
                    }
                }
                is HighlightsPageNavKey -> {
                    NavEntry(
                        key = key
                    ) {
                        HighlightsPage(
                            {
                                CustomNavigationBar(
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
                                    onClick = { index ->
                                        when (index) {
                                            0 -> backStack.add(ContactListPageNavKey)
                                            1 -> backStack.add(HighlightsPageNavKey)
                                            2 -> backStack.add(ManagePageNavKey)
                                        }
                                    },
                                    selectedIndex = 1
                                )
                            }
                        )
                    }
                }
                is ManagePageNavKey -> {
                    NavEntry(
                        key = key
                    ) {
                        ManagePage(
                            {
                                CustomNavigationBar(
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
                                    onClick = { index ->
                                        when (index) {
                                            0 -> backStack.add(ContactListPageNavKey)
                                            1 -> backStack.add(HighlightsPageNavKey)
                                            2 -> backStack.add(ManagePageNavKey)
                                        }
                                    },
                                    selectedIndex = 2
                                )
                            }
                        )
                    }
                }
                is SettingsPageNavKey -> {
                    NavEntry(
                        key = key
                    ) {
                        SettingsPage()
                    }
                }
                else -> {
                    NavEntry(
                        key = key
                    ) {
                        ContactListPage(
                            {
                                CustomNavigationBar(
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
                                    onClick = { index ->
                                        when (index) {
                                            0 -> backStack.add(ContactListPageNavKey)
                                            1 -> backStack.add(HighlightsPageNavKey)
                                            2 -> backStack.add(ManagePageNavKey)
                                        }
                                    },
                                    selectedIndex = 0
                                )
                            }
                        )
                    }
                }
            }
        }
    )
}