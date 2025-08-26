package de.benkralex.socius.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import de.benkralex.socius.R
import de.benkralex.socius.data.contacts.contacts
import de.benkralex.socius.data.contacts.loadContacts
import de.benkralex.socius.data.settings.loadSettings
import de.benkralex.socius.pages.AllowPermissionsPage
import de.benkralex.socius.pages.ContactDetailPage
import de.benkralex.socius.pages.ContactListPage
import de.benkralex.socius.pages.EditContactPage
import de.benkralex.socius.pages.HighlightsPage
import de.benkralex.socius.pages.ManagePage
import de.benkralex.socius.pages.SettingsPage
import de.benkralex.socius.widgets.CustomNavigationBar
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
data object AllowPermissionsPageNavKey: NavKey
@Serializable
data class ContactDetailIntentNavKey(val contactId: String): NavKey
@Serializable
data class ContactDetailPageNavKey(val contactId: Int): NavKey
@Serializable
data class ContactEditPageNavKey(val contactId: Int? = null): NavKey

lateinit var backStack: NavBackStack

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NavigationRoot(
    modifier: Modifier
) {
    val context = LocalContext.current
    loadSettings(context)
    backStack = rememberNavBackStack(AllowPermissionsPageNavKey)
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
                is AllowPermissionsPageNavKey -> {
                    NavEntry(
                        key = key
                    ) {
                        AllowPermissionsPage(
                            modifier = Modifier
                                .clip(RoundedCornerShape(24.dp)),
                            onAllPermissionsAllowed = {
                                loadContacts()
                                backStack.clear()
                                backStack.add(ContactListPageNavKey)
                            }
                        )
                    }
                }
                is ContactListPageNavKey -> {
                    NavEntry(
                        key = key
                    ) {
                        ContactListPage(
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
                            onContactSelected = { contactId ->
                                backStack.add(
                                    ContactDetailPageNavKey(contactId)
                                )
                            },
                            onNewContactCreate = {
                                backStack.add(ContactEditPageNavKey(null))
                            },
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
                            },
                            navigateSettings = {
                                backStack.add(SettingsPageNavKey)
                            },
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
                        ContactDetailPage(
                            modifier = Modifier
                                .clip(RoundedCornerShape(24.dp)),
                            contact = contacts[key.contactId],
                            onBackClick = {
                                backStack.removeAt(backStack.size - 1)
                            },
                            onEditClick = {
                                backStack.add(
                                    ContactEditPageNavKey(key.contactId)
                                )
                            }
                        )
                    }
                }
                is ContactDetailIntentNavKey -> {
                    NavEntry(
                        key = key
                    ) {
                        key (contacts) {
                            val index = contacts.indexOf(contacts.firstOrNull { it.id == key.contactId })
                            if (index == -1) {
                                Column (
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(MaterialTheme.colorScheme.background),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                ) {
                                    LoadingIndicator()
                                }
                            } else {
                                backStack.clear()
                                backStack.add(
                                    ContactListPageNavKey
                                )
                                backStack.add(
                                    ContactDetailPageNavKey(index)
                                )
                            }
                        }
                    }
                }
                is ContactEditPageNavKey -> {
                    NavEntry(
                        key = key
                    ) {
                        EditContactPage(
                            modifier = Modifier
                                .clip(RoundedCornerShape(24.dp)),
                            contactId = key.contactId,
                            onBackClick = {
                                backStack.removeAt(backStack.size - 1)
                            }
                        )
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