package de.benkralex.socius.data.contacts.system.load

import android.content.ContentResolver
import android.provider.ContactsContract

fun loadNotesBatch(contentResolver: ContentResolver, contactIds: List<String>): Map<String, String?> {
    val result = mutableMapOf<String, String?>()
    if (contactIds.isEmpty()) return result
    val selection = "${ContactsContract.Data.CONTACT_ID} IN (${contactIds.joinToString(",")}) AND ${ContactsContract.Data.MIMETYPE} = ?"
    val selectionArgs = arrayOf(ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)
    val cursor = contentResolver.query(
        ContactsContract.Data.CONTENT_URI,
        null,
        selection,
        selectionArgs,
        null
    )
    cursor?.use {
        val contactIdIndex = it.getColumnIndex(ContactsContract.Data.CONTACT_ID)
        val noteIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE)
        while (it.moveToNext()) {
            val contactId = it.getString(contactIdIndex)
            val note = if (noteIndex != -1) it.getString(noteIndex) else null
            result[contactId] = note
        }
    }
    return result
}