package de.benkralex.socius.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import de.benkralex.socius.data.Contact
import de.benkralex.socius.data.contacts.system.updateStarred
import de.benkralex.socius.widgets.contactInformation.CustomFieldsWidget
import de.benkralex.socius.widgets.contactInformation.EmailsWidget
import de.benkralex.socius.widgets.contactInformation.EventsWidget
import de.benkralex.socius.widgets.contactInformation.GroupsWidget
import de.benkralex.socius.widgets.contactInformation.NoteWidget
import de.benkralex.socius.widgets.contactInformation.PhoneNumbersWidget
import de.benkralex.socius.widgets.contactInformation.PostalAddressesWidget
import de.benkralex.socius.widgets.contactInformation.ProfileWithName
import de.benkralex.socius.widgets.contactInformation.RelationsWidget
import de.benkralex.socius.widgets.contactInformation.WebsitesWidget
import de.benkralex.socius.widgets.contactInformation.SmallInformationWidget


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailPage(
    modifier: Modifier = Modifier,
    contact: Contact,
    onBackClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    ProfileWithName(
                        contact = contact,
                    )
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
                actions = {
                    val context = LocalContext.current
                    var isStarred by remember { mutableStateOf(contact.isStarred) }
                    if (isStarred) {
                        Icon(
                            Icons.Outlined.Star,
                            "is starred",
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable(
                                    onClick = {
                                        contact.isStarred = false
                                        isStarred = false
                                        updateStarred(
                                            contactId = contact.id,
                                            value = false,
                                            context = context,
                                        )
                                    }
                                )
                        )
                    } else {
                        Icon(
                            Icons.Outlined.StarOutline,
                            "is not starred",
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable(
                                    onClick = {
                                        contact.isStarred = true
                                        isStarred = true
                                        updateStarred(
                                            contactId = contact.id,
                                            value = true,
                                            context = context,
                                        )
                                    }
                                )
                        )
                    }
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable(
                                onClick = onEditClick
                            ),
                    )
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .fillMaxWidth()
        ) {
            NoteWidget(contact.note)
            SmallInformationWidget(contact)
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
}