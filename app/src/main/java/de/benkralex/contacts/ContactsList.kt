package de.benkralex.contacts

import android.R.attr.contentDescription
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.benkralex.contacts.backend.Contact
import de.benkralex.contacts.backend.PhoneNumber

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactsList(
    contacts: List<Contact>,
    paddingValues: PaddingValues
) {
    // Group contacts by the first letter of their name
    val grouped = contacts.groupBy {
        it.displayName?.firstOrNull()?.uppercase() ?: "?"
    }.toSortedMap(compareBy { it })
    // Display the contacts in a LazyColumn with sticky headers
    LazyColumn(
        contentPadding = paddingValues,
        modifier = Modifier.fillMaxWidth()
    ) {
        grouped.forEach { (initial, contactsForInitial) ->
            stickyHeader {
                ContactsListHeading(
                    text = initial.toString()
                )
            }
            items(contactsForInitial) { c ->
                ContactCard(
                    name = c.displayName ?: "Unknown",
                    profilePicture = c.photoBitmap,
                )
            }
        }
    }
}

@Composable
fun ContactsListHeading(
    text: String,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0, 0, 0, 1),
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            style = TextStyle(
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun ContactCard(
    name: String,
    profilePicture: Bitmap? = null,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0, 0, 0, 1),
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            if (profilePicture != null) {
                Image(
                    bitmap = profilePicture.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.profile_picture),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = name)
            }
        }
    }
}