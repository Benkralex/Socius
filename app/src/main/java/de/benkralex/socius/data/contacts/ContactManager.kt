package de.benkralex.socius.data.contacts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import de.benkralex.socius.data.Contact
import de.benkralex.socius.data.getAndroidSystemContacts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

var contacts by mutableStateOf<MutableList<Contact>>(mutableListOf())
    private set

private var systemContacts: List<Contact> = mutableListOf()
private var localContacts: List<Contact>  = mutableListOf()
private var remoteContacts: Map<String, List<Contact>>  = mutableMapOf()

private fun updateContacts() {
    contacts = (systemContacts + localContacts + remoteContacts.values.flatten()).toMutableList()
}

@Composable
fun LoadContacts() {
    LoadSystemContacts()
    LoadLocalContacts()
    LoadRemoteContacts()
}

@Composable
private fun LoadSystemContacts() {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val loadedContacts = getAndroidSystemContacts(context = context)
            withContext(Dispatchers.Main) {
                systemContacts = loadedContacts
                updateContacts()
            }
        }
    }
}

@Composable
private fun LoadLocalContacts() {
    // This function is a placeholder for loading local contacts.
    localContacts = emptyList()
}

@Composable
private fun LoadRemoteContacts() {
    // This function is a placeholder for loading remote contacts.
    remoteContacts = emptyMap()
}