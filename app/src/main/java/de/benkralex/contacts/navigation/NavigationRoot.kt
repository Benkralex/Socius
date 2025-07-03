package de.benkralex.contacts.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
import de.benkralex.contacts.backend.settings.loadSettings
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
    var contacts by remember { mutableStateOf<List<Contact>?>(null) }
    val context = LocalContext.current
    loadSettings(context)
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
        popTransitionSpec = {
            fadeIn(
                animationSpec = tween(
                    500,
                    easing = FastOutSlowInEasing
                ),
                initialAlpha = 1.0f,
            ) togetherWith fadeOut(
                animationSpec = tween(
                    500,
                    easing = FastOutSlowInEasing
                ),
                targetAlpha = 1.0f,
            )
        },
        transitionSpec = {
            fadeIn(
                animationSpec = tween(
                    500,
                    easing = FastOutSlowInEasing
                ),
                initialAlpha = 1.0f,
            ) togetherWith fadeOut(
                animationSpec = tween(
                    500,
                    easing = FastOutSlowInEasing
                ),
                targetAlpha = 1.0f,
            )
        },
        predictivePopTransitionSpec = {
            ContentTransform(
                targetContentEnter = fadeIn(
                    animationSpec = spring(
                        dampingRatio = 1.0f,
                        stiffness = 16000.0f,
                    )
                ),
                initialContentExit = scaleOut(targetScale = 0.7f),
            )
        },
        entryProvider = { key ->
            when(key) {
                is ContactListPageNavKey -> {
                    NavEntry(
                        key = key
                    ) {
                        ContactListPage(
                            modifier = Modifier
                                .clip(RoundedCornerShape(24.dp)),
                            contacts = contacts,
                            menuBar = {
                                CustomNavigationBar(
                                    items = listOf(
                                        stringResource(R.string.page_contacts),
                                        stringResource(R.string.page_highlights),
                                        stringResource(R.string.page_manage)
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
                                            1 -> {
                                                backStack.clear()
                                                backStack.add(ContactListPageNavKey)
                                                backStack.add(HighlightsPageNavKey)
                                            }
                                            2 -> {
                                                backStack.clear()
                                                backStack.add(ContactListPageNavKey)
                                                backStack.add(ManagePageNavKey)
                                            }
                                        }
                                    },
                                    selectedIndex = 0
                                )
                            },
                            onContactSelected = { contactIdx ->
                                backStack.add(
                                    ContactDetailPageNavKey(contactIdx)
                                )
                            },
                            onSettingsSelected = {
                                backStack.add(SettingsPageNavKey)
                            }
                        )
                    }
                }
                is HighlightsPageNavKey -> {
                    NavEntry(
                        key = key
                    ) {
                        HighlightsPage(
                            modifier = Modifier
                                .clip(RoundedCornerShape(24.dp)),
                            menuBar = {
                                CustomNavigationBar(
                                    items = listOf(
                                        stringResource(R.string.page_contacts),
                                        stringResource(R.string.page_highlights),
                                        stringResource(R.string.page_manage)
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
                                            0 -> {
                                                backStack.clear()
                                                backStack.add(ContactListPageNavKey)
                                            }
                                            2 -> {
                                                backStack.clear()
                                                backStack.add(ContactListPageNavKey)
                                                backStack.add(ManagePageNavKey)
                                            }
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
                            modifier = Modifier
                                .clip(RoundedCornerShape(24.dp)),
                            menuBar = {
                                CustomNavigationBar(
                                    items = listOf(
                                        stringResource(R.string.page_contacts),
                                        stringResource(R.string.page_highlights),
                                        stringResource(R.string.page_manage)
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
                                            0 -> {
                                                backStack.clear()
                                                backStack.add(ContactListPageNavKey)
                                            }
                                            1 -> {
                                                backStack.clear()
                                                backStack.add(ContactListPageNavKey)
                                                backStack.add(HighlightsPageNavKey)
                                            }
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
                        SettingsPage(
                            modifier = Modifier
                                .clip(RoundedCornerShape(24.dp)),
                            onBackClick = {
                                backStack.removeAt(backStack.size - 1)
                            }
                        )
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
                                modifier = Modifier
                                    .clip(RoundedCornerShape(24.dp)),
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
                        throw IllegalStateException("Unhandled navigation key: $key")
                    }
                }
            }
        }
    )
}