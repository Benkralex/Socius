package de.benkralex.contacts.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.benkralex.contacts.R
import de.benkralex.contacts.backend.Contact
import de.benkralex.contacts.backend.Email
import de.benkralex.contacts.backend.PhoneNumber
import de.benkralex.contacts.backend.PostalAddress
import de.benkralex.contacts.widgets.contactInfromationWidgets.EmailsWidget
import de.benkralex.contacts.widgets.contactInfromationWidgets.PhoneNumbers
import de.benkralex.contacts.widgets.contactInfromationWidgets.PostalAddresses
import de.benkralex.contacts.widgets.contactInfromationWidgets.ProfileWithName


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailPage(
    contact: Contact,
    onBackClick: () -> Unit = {}
) {
    Scaffold(
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
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .fillMaxWidth()
        ) {
            EmailsWidget(contact.emails)
            PhoneNumbers(contact.phoneNumbers)
            PostalAddresses(contact.addresses)
        }
    }
}