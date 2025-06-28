package de.benkralex.contacts.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
                    contact.displayName?.let { Text(it) }
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier
                            .clickable(
                                onClick = onBackClick
                            )
                    )
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
        ) {
            Row (
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )
            {
                if (contact.photoBitmap != null) {
                    Image(
                        bitmap = contact.photoBitmap!!.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .align(Alignment.CenterVertically)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.profile_picture),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.DarkGray)
                            .align(Alignment.CenterVertically)
                    )
                }
                contact.displayName?.let {
                    Text(
                        text = it,
                        modifier = Modifier
                            .padding(top = 8.dp, start = 10.dp)
                            .align(Alignment.CenterVertically),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
            contact.emails.firstOrNull()?.let { email: Email ->
                Text(
                    text = "E-Mail: ${email.address}",
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            contact.phoneNumbers.firstOrNull()?.let { phone: PhoneNumber ->
                Text(
                    text = "Telefonnummer: ${phone.number}",
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            contact.addresses.firstOrNull()?.let { address: PostalAddress ->
                Text(
                    text = "Adresse: ${address.street}, ${address.city}, ${address.postcode}, ${address.country}",
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}