package de.benkralex.socius.data.contacts

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import de.benkralex.socius.MainActivity
import de.benkralex.socius.data.contacts.local.load.getLocalContacts
import de.benkralex.socius.data.model.Contact
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

val contacts by derivedStateOf {
    localContacts.toMutableList()
}

val groups by derivedStateOf {
    contacts.map { it.groups }.flatten().toSet().toList()
}

val loadingContacts by derivedStateOf { loadingLocalContacts }
private var loadingLocalContacts by mutableStateOf(false)
private var localContacts by mutableStateOf<List<Contact>>(emptyList())

fun loadAllContacts() {
    val mainActivity = MainActivity
    Thread {
        runBlocking {
            launch {
                if (!loadingLocalContacts) {
                    loadingLocalContacts = true
                    loadLocalContacts(mainActivity)
                    loadingLocalContacts = false
                }
            }
        }
    }.start()
}

private suspend fun loadLocalContacts(mainActivity: MainActivity.Companion) {
    localContacts = getLocalContacts(mainActivity)
}