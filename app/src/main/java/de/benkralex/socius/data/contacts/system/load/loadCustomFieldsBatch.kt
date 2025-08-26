package de.benkralex.socius.data.contacts.system.load

import android.content.ContentResolver
import android.provider.ContactsContract

fun loadCustomFieldsBatch(contentResolver: ContentResolver, contactIds: List<String>): Map<String, Map<String, String>> {
    val result = mutableMapOf<String, MutableMap<String, String>>()
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
        val keyIndex = it.getColumnIndex(ContactsContract.Data.DATA1)
        val valueIndex = it.getColumnIndex(ContactsContract.Data.DATA2)
        while (it.moveToNext()) {
            val contactId = it.getString(contactIdIndex)
            val key = if (keyIndex != -1) it.getString(keyIndex) else null
            val value = if (valueIndex != -1) it.getString(valueIndex) else null
            if (key != null && value != null) {
                if (result[contactId] == null) result[contactId] = mutableMapOf()
                result[contactId]?.put(key, value)
            }
        }
    }
    return result
}