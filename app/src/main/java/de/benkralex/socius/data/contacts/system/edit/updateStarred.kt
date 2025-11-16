package de.benkralex.socius.data.contacts.system.edit

import android.content.ContentProviderOperation
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import de.benkralex.socius.data.contacts.loadAllContacts

fun updateStarred(contactId: String?, value: Boolean, context: Context) {
    if (contactId == null) {
        return
    }
    val operations = ArrayList<ContentProviderOperation>()
    operations.add(
        ContentProviderOperation.newUpdate(Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contactId))
            .withValue(ContactsContract.RawContacts.STARRED, if (value) 1 else 0)
            .build()
    )
    val contentResolver = context.contentResolver
    try {
        contentResolver.applyBatch(ContactsContract.AUTHORITY, operations)
    } catch (e: Exception) {
        Log.e("updateStarred", "Fehler bei applyBatch: ${e.message}", e)
    }
    loadAllContacts()
}