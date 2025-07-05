package de.benkralex.socius.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.benkralex.socius.backend.Contact
import de.benkralex.socius.widgets.contactInfromationWidgets.CustomFieldsWidget
import de.benkralex.socius.widgets.contactInfromationWidgets.EmailsWidget
import de.benkralex.socius.widgets.contactInfromationWidgets.EventsWidget
import de.benkralex.socius.widgets.contactInfromationWidgets.GroupsWidget
import de.benkralex.socius.widgets.contactInfromationWidgets.NoteWidget
import de.benkralex.socius.widgets.contactInfromationWidgets.PhoneNumbersWidget
import de.benkralex.socius.widgets.contactInfromationWidgets.PostalAddressesWidget
import de.benkralex.socius.widgets.contactInfromationWidgets.ProfileWithName
import de.benkralex.socius.widgets.contactInfromationWidgets.RelationsWidget
import de.benkralex.socius.widgets.contactInfromationWidgets.WebsitesWidget
import de.benkralex.socius.widgets.contactInfromationWidgets.SmallInformationWidget


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailPage(
    modifier: Modifier = Modifier,
    contact: Contact,
    onBackClick: () -> Unit = {}
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
                    if (contact.isStarred) {
                        Icon(
                            Icons.Outlined.Star,
                            "is starred",
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    } else {
                        Icon(
                            Icons.Outlined.StarOutline,
                            "is mot starred",
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }
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