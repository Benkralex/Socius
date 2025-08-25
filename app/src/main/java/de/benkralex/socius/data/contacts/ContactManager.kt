package de.benkralex.socius.data.contacts

import android.content.Context
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import de.benkralex.socius.MainActivity
import de.benkralex.socius.data.Contact
import de.benkralex.socius.data.contacts.system.getAndroidSystemContacts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

val contacts by derivedStateOf {
    (systemContacts + localContacts + remoteContacts.values.flatten()).toMutableList()
}

private var systemContacts by mutableStateOf<List<Contact>>(emptyList())
private var localContacts by mutableStateOf<List<Contact>>(emptyList())
private var remoteContacts by mutableStateOf<Map<String, List<Contact>>>(emptyMap())

fun loadContacts() {
    val ctx = MainActivity.instance
    Thread {
        runBlocking {
            launch {
                loadSystemContacts(ctx)
            }
        }
    }.start()
    Thread {
        runBlocking {
            launch {
                loadLocalContacts(ctx)
            }
        }
    }.start()
    Thread {
        runBlocking {
            launch {
                loadRemoteContacts(ctx)
            }
        }
    }.start()
}

private suspend fun loadSystemContacts(ctx: Context) {
    withContext(Dispatchers.IO) {
        val loadedContacts = getAndroidSystemContacts(context = ctx)
        withContext(Dispatchers.Main) {
            systemContacts = loadedContacts
        }
    }
}

private suspend fun loadLocalContacts(ctx: Context) {
    // This function is a placeholder for loading local contacts.
    localContacts = emptyList()
}

private suspend fun loadRemoteContacts(ctx: Context) {
    // This function is a placeholder for loading remote contacts.
    remoteContacts = emptyMap()
}