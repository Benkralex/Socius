package de.benkralex.socius.data.import_export

import android.graphics.Bitmap
import de.benkralex.socius.MainActivity
import de.benkralex.socius.data.Contact
import de.benkralex.socius.data.contacts.loadAllContacts
import de.benkralex.socius.data.contacts.local.database.LocalContactsEntity
import de.benkralex.socius.sync.SyncManager
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun loadPicture(link: String): Bitmap {
    val url = java.net.URL(link)
    val connection = url.openConnection() as java.net.HttpURLConnection
    connection.doInput = true
    connection.connect()
    val input = connection.inputStream
    return android.graphics.BitmapFactory.decodeStream(input)
}

fun importContacts(contacts: List<Contact>) {
    Thread {
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
    }.start()
}