package de.benkralex.socius.widgets

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.benkralex.socius.data.contacts.contacts
import de.benkralex.socius.data.settings.getFormattedName

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ContactsList(
    modifier: Modifier = Modifier,
    onContactSelected: (Int) -> Unit = {},
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredContacts by remember {
        derivedStateOf {
            if (searchQuery.isNotBlank()) {
                contacts.filter {
                    getFormattedName(it).contains(searchQuery, ignoreCase = true)
                }
            } else {
                contacts
            }
        }
    }

    val grouped by remember {
        derivedStateOf {
            filteredContacts.groupBy {
                if (it.isStarred) "starred"
                else getFormattedName(it).firstOrNull()?.uppercase() ?: "?"
            }.toSortedMap(compareBy {
                when (it) {
                    "starred" -> "AA"
                    "?" -> "A"
                    else -> it + "B"
                }
            })
        }
    }

    Column (
        modifier = modifier
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .padding(8.dp)
                .padding(horizontal = 12.dp)
                .fillMaxWidth(),
            placeholder = { Text("Search") },
            singleLine = true,
            leadingIcon = {
                Icon(
                    modifier = Modifier
                        .padding(start = 8.dp),
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Icon",
                )
            },
            shape = RoundedCornerShape(32.dp),
        )
        LazyColumn {
            grouped.forEach { (initial, contactsForInitial) ->
                stickyHeader {
                    if (initial == "starred") {
                        ContactsListHeading(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Starred"
                        )
                    } else {
                        ContactsListHeading(
                            text = initial
                        )
                    }
                }
                items(contactsForInitial) { c ->
                    Log.d("ContactsList", "Displaying contact: ${getFormattedName(c)}")
                    ContactCard(
                        name = getFormattedName(c),
                        profilePicture = c.photoBitmap,
                        modifier = Modifier.clickable(
                            onClick = { onContactSelected(contacts.indexOf(c)) }
                        )
                    )
                }
            }
        }
    }
}



















