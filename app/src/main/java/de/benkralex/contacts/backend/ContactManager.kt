package de.benkralex.contacts.backend

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

val contacts: List<Contact>
    get() = systemContacts

var systemContacts = mutableListOf<Contact>()

@Composable
fun LoadSystemContacts() {
    systemContacts.clear()
    var contacts by remember { mutableStateOf<List<Contact>?>(null) }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            contacts = getAndroidSystemContacts(context = context)
        }
    }
    systemContacts.addAll(contacts ?: emptyList())
}