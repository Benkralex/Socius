package de.benkralex.socius.data.import_export.common

import de.benkralex.socius.MainActivity
import de.benkralex.socius.data.model.Contact
import de.benkralex.socius.data.contacts.loadAllContacts
import de.benkralex.socius.data.contacts.local.database.LocalContactsEntity
import de.benkralex.socius.data.syncadapter.SyncManager
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun importContacts(contacts: List<Contact>) {
    val thread = Thread {
        runBlocking {
            launch {
                for (c in contacts) {
                    MainActivity.localContactsDao.insert(
                        LocalContactsEntity().fromContact(c)
                    )
                }
                loadAllContacts()
                SyncManager.requestSync(MainActivity.instance)
                return@launch
            }
        }
    }
    thread.start()
    thread.join()
}