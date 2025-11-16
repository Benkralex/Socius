package de.benkralex.socius.data.contacts

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import de.benkralex.socius.MainActivity
import de.benkralex.socius.data.Contact
import de.benkralex.socius.data.contacts.local.load.getLocalContacts
import de.benkralex.socius.data.contacts.system.load.getAndroidSystemContacts
import de.benkralex.socius.data.settings.loadAndroidSystemContacts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

val contacts by derivedStateOf {
    ((if (loadAndroidSystemContacts) systemContacts else mutableListOf())
            + localContacts
            + remoteContacts.values.flatten()
    ).toMutableList()
}

val groups by derivedStateOf {
    contacts.map { it.groups }.flatten().toSet().toList()
}

val loadingContacts by derivedStateOf { loadingSystemContacts || loadingLocalContacts || loadingRemoteContacts }
private var loadingSystemContacts by mutableStateOf(false)
private var loadingLocalContacts by mutableStateOf(false)
private var loadingRemoteContacts by mutableStateOf(false)
private var systemContacts by mutableStateOf<List<Contact>>(emptyList())
private var localContacts by mutableStateOf<List<Contact>>(emptyList())
private var remoteContacts by mutableStateOf<Map<String, List<Contact>>>(emptyMap())

fun loadAllContacts() {
    val mainActivity = MainActivity
    Thread {
        runBlocking {
            launch {
                if (!loadingSystemContacts) {
                    loadingSystemContacts = true
                    loadSystemContacts(mainActivity)
                    loadingSystemContacts = false
                    println("Loaded system contacts: ${systemContacts.size}")
                }
            }
        }
    }.start()
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
    Thread {
        runBlocking {
            launch {
                if (!loadingRemoteContacts) {
                    loadingRemoteContacts = true
                    loadRemoteContacts(mainActivity)
                    loadingRemoteContacts = false
                }
            }
        }
    }.start()
}

fun reloadSystemContacts() {
    val mainActivity = MainActivity
    Thread {
        runBlocking {
            launch {
                if (!loadingSystemContacts) {
                    loadingSystemContacts = true
                    loadSystemContacts(mainActivity)
                    loadingSystemContacts = false
                    println("Loaded system contacts: ${systemContacts.size}")
                }
            }
        }
    }.start()
}

fun reloadLocalContacts() {
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

fun reloadRemoteContacts() {
    val mainActivity = MainActivity
    Thread {
        runBlocking {
            launch {
                if (!loadingRemoteContacts) {
                    loadingRemoteContacts = true
                    loadRemoteContacts(mainActivity)
                    loadingRemoteContacts = false
                }
            }
        }
    }.start()
}

private suspend fun loadSystemContacts(mainActivity: MainActivity.Companion) {
    if (!loadAndroidSystemContacts) return
    withContext(Dispatchers.IO) {
        val loadedContacts = getAndroidSystemContacts(context = mainActivity.instance)
        withContext(Dispatchers.Main) {
            systemContacts = loadedContacts
            println("System contacts loaded: ${systemContacts.size}" )
        }
    }
}

private suspend fun loadLocalContacts(mainActivity: MainActivity.Companion) {
    localContacts = getLocalContacts(mainActivity)
}

private suspend fun loadRemoteContacts(mainActivity: MainActivity.Companion) {
    // This function is a placeholder for loading remote contacts.
    remoteContacts = emptyMap()
}