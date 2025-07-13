package de.benkralex.socius.data.contacts.system

import android.content.ContentProviderOperation
import android.content.Context
import android.provider.ContactsContract
import android.util.Log


fun updateStarred(contactId: String?, value: Boolean, context: Context) {
    Log.d("updateStarred", "Start: contactId=$contactId, value=$value")
    if (contactId == null) {
        Log.d("updateStarred", "Abbruch: contactId ist null")
        return
    }
    val operations = ArrayList<ContentProviderOperation>()
    operations.add(
        ContentProviderOperation.newUpdate(ContactsContract.RawContacts.CONTENT_URI)
            .withSelection("${ContactsContract.RawContacts._ID} = ?", arrayOf(contactId))
            .withValue(ContactsContract.RawContacts.STARRED, if (value) 1 else 0)
            .build()
    )
    if (value) {
        operations.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValue(ContactsContract.Data.RAW_CONTACT_ID, contactId)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE)
                .withValue(
                    ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID,
                    "Starred in Android"
                )
                .build()
        )
    } else {
        operations.add(
            ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
                .withSelection(
                    "${ContactsContract.Data.RAW_CONTACT_ID}=? AND ${ContactsContract.Data.MIMETYPE}=? AND ${ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID}=?",
                    arrayOf(
                        contactId,
                        ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE,
                        "Starred in Android",
                    )
                )
                .build()
        )
    }
    Log.d("updateStarred", "Update-Operation hinzugefügt für contactId=$contactId, STARRED=${if (value) 1 else 0}")
    val contentResolver = context.contentResolver
    try {
        contentResolver.applyBatch(ContactsContract.AUTHORITY, operations)
        Log.d("updateStarred", "applyBatch erfolgreich ausgeführt")
    } catch (e: Exception) {
        Log.e("updateStarred", "Fehler bei applyBatch: ${e.message}", e)
    }
}