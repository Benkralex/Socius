package de.benkralex.contacts.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import de.benkralex.contacts.backend.Contact
import de.benkralex.contacts.backend.getAndroidSystemContacts
import de.benkralex.contacts.pages.ContactDetailPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
data class ContactDetailPageNavKey(val contactId: Int): NavKey


@Composable
fun NavigationRoot(
    modifier: Modifier
) {
    val context = LocalContext.current
    var contacts by remember { mutableStateOf<List<Contact>?>(null) }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            contacts = getAndroidSystemContacts(context = context)
        }
    }

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
                            contacts = contacts,
                            menuBar = {
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
                            },
                            onContactSelected = { contactIdx ->
                                backStack.add(
                                    ContactDetailPageNavKey(contactIdx)
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
                is ContactDetailPageNavKey -> {
                    NavEntry(
                        key = key
                    ) {
                        if (contacts?.get(key.contactId) == null) {
                            throw IllegalArgumentException(
                                "Contact with ID ${key.contactId} not found in contacts list."
                            )
                        } else {
                            ContactDetailPage(
                                contact = contacts?.get(key.contactId)!!,
                                onBackClick = {
                                    backStack.removeAt(backStack.size - 1)
                                }
                            )
                        }
                    }
                }
                else -> {
                    NavEntry(
                        key = key
                    ) {
                        ContactListPage(
                            contacts = contacts,
                            menuBar = {
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
                            },
                            onContactSelected = { contactIdx ->
                                backStack.add(
                                    ContactDetailPageNavKey(contactIdx)
                                )
                            }
                        )
                    }
                }
            }
        }
    )
}