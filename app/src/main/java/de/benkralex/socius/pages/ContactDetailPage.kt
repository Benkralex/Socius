package de.benkralex.socius.pages

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import de.benkralex.socius.R
import de.benkralex.socius.data.Contact
import de.benkralex.socius.data.contacts.deleteContact
import de.benkralex.socius.data.contacts.editStarredStatus
import de.benkralex.socius.data.settings.getFormattedName
import de.benkralex.socius.widgets.contactInformation.CustomFieldsWidget
import de.benkralex.socius.widgets.contactInformation.EmailsWidget
import de.benkralex.socius.widgets.contactInformation.EventsWidget
import de.benkralex.socius.widgets.contactInformation.GroupsWidget
import de.benkralex.socius.widgets.contactInformation.NoteWidget
import de.benkralex.socius.widgets.contactInformation.PhoneNumbersWidget
import de.benkralex.socius.widgets.contactInformation.PostalAddressesWidget
import de.benkralex.socius.widgets.contactInformation.ProfilePicture
import de.benkralex.socius.widgets.contactInformation.ProfileWithName
import de.benkralex.socius.widgets.contactInformation.RelationsWidget
import de.benkralex.socius.widgets.contactInformation.WebsitesWidget
import de.benkralex.socius.widgets.contactInformation.WorkInformationWidget
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Thread.sleep
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailPage(
    modifier: Modifier = Modifier,
    contact: Contact,
    onBackClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
) {
    var showProfileFullscreen by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    var showDeletionConfirmationDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    val startFadeScrollValue = 420
                    val endFadeScrollValue = min(500, scrollState.maxValue)
                    val alpha = if (scrollState.value > endFadeScrollValue) {
                        1f
                    } else if (scrollState.value < startFadeScrollValue) {
                        0f
                    } else {
                        (scrollState.value - startFadeScrollValue) / (endFadeScrollValue - startFadeScrollValue).toFloat()
                    }
                    ProfileWithName(
                        modifier = Modifier
                            .alpha(alpha),
                        contact = contact,
                        onProfileClick = {
                            if (contact.photoBitmap != null) {
                                showProfileFullscreen = true
                            }
                        }
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.content_desc_back),
                        modifier = Modifier
                            .padding(5.dp)
                            .clickable(
                                onClick = onBackClick
                            )
                    )
                },
                actions = {
                    var isStarred by remember { mutableStateOf(contact.isStarred) }
                    val starIcon = if (isStarred) Icons.Outlined.Star else Icons.Outlined.StarOutline
                    val starDescription = "${stringResource(R.string.content_desc_toggle_starred)} (${
                        if (isStarred) {
                            stringResource(R.string.starred)
                        } else {
                            stringResource(R.string.not_starred)
                        }
                    })"

                    LaunchedEffect (contact.isStarred) {
                        isStarred = contact.isStarred
                    }
                    IconButton(
                        onClick = {
                            if (contact.isReadOnly()) return@IconButton
                            isStarred = !isStarred
                            Thread {
                                runBlocking {
                                    launch {
                                        editStarredStatus(
                                            contact = contact,
                                            isStarred = isStarred,
                                        )
                                    }
                                }
                            }.start()
                        },
                    ) {
                        Icon(
                            imageVector = starIcon,
                            contentDescription = starDescription,
                            modifier = Modifier
                                .padding(8.dp),
                        )
                    }

                    if (!contact.isReadOnly()) {
                        IconButton(
                            onClick = onEditClick
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = stringResource(R.string.content_desc_edit),
                                modifier = Modifier
                                    .padding(8.dp),
                            )
                        }
                    }
                    IconButton(
                        onClick = {
                            showDeletionConfirmationDialog = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = stringResource(R.string.content_desc_edit),
                            modifier = Modifier
                                .padding(8.dp),
                        )
                    }
                }
            )
        },
    ) { paddingValues ->
        if (showDeletionConfirmationDialog) {
            BasicAlertDialog(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        shape = RoundedCornerShape(CornerSize(30.dp))
                    )
                    .padding(32.dp)
                    .fillMaxWidth(),
                onDismissRequest = {
                    showDeletionConfirmationDialog = false
                },
                content = {
                    Column {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = "Delete " + getFormattedName(contact) + "?",
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(Modifier.height(20.dp))
                        Button(
                            modifier = Modifier
                                .fillMaxWidth(),
                            onClick = {
                                if (contact.isReadOnly()) return@Button
                                onBackClick()
                                Thread {
                                    runBlocking {
                                        launch {
                                            sleep(500)
                                            deleteContact(contact)
                                        }
                                    }
                                }.start()
                            },
                            colors = ButtonDefaults.buttonColors().copy(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                            ),
                        ) {
                            Text(
                                text = "Delete"
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Button(
                            modifier = Modifier
                                .fillMaxWidth(),
                            onClick = {
                                showDeletionConfirmationDialog = false
                            },
                        ) {
                            Text(
                                text = "Cancel"
                            )
                        }
                    }
                },
            )
        }
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(paddingValues)
                .fillMaxWidth()
        ) {
            ProfilePicture(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp, bottom = 8.dp)
                    .clickable {
                        if (contact.photoBitmap != null) {
                            showProfileFullscreen = true
                        }
                    },
                contact = contact,
                size = 150.dp,
            )
            Text(
                text = getFormattedName(contact),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            NoteWidget(contact.note)
            WorkInformationWidget(contact)
            GroupsWidget(groups = contact.groups)
            EmailsWidget(contact.emails)
            PhoneNumbersWidget(contact.phoneNumbers)
            PostalAddressesWidget(contact.addresses)
            EventsWidget(contact.events)
            RelationsWidget(contact.relations)
            WebsitesWidget(contact.websites)
            CustomFieldsWidget(contact.customFields)
        }
    }
    if (contact.photoBitmap != null && showProfileFullscreen) {
        BackHandler {
            showProfileFullscreen = false
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0f, 0f, 0f, 0.7f), RectangleShape)
                .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
                    showProfileFullscreen = false
                },
            contentAlignment = Alignment.Center,
        ) {
            Image(
                bitmap = contact.photoBitmap!!.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {}
            )
        }
    }
}