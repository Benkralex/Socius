package de.benkralex.socius.data.contacts.system.load

import android.content.ContentResolver
import android.provider.ContactsContract

fun loadStarredBatch(contentResolver: ContentResolver, contactIds: List<String>): Map<String, Boolean> {
    val result = mutableMapOf<String, Boolean>()

    if (contactIds.isEmpty()) return result

    val selection = "${ContactsContract.Data.CONTACT_ID} IN (${contactIds.joinToString(",")})"
    val cursor = contentResolver.query(
        ContactsContract.Data.CONTENT_URI,
        arrayOf(ContactsContract.Data.CONTACT_ID, ContactsContract.Data.STARRED),
        selection,
        null,
        null
    )

    cursor?.use {
        val idIndex = it.getColumnIndex(ContactsContract.Data.CONTACT_ID)
        val starredIndex = it.getColumnIndex(ContactsContract.Data.STARRED)

        while (it.moveToNext()) {
            val contactId = it.getString(idIndex)
            val isStarred = it.getInt(starredIndex) == 1
            result[contactId] = isStarred
        }
    }

    return result
}