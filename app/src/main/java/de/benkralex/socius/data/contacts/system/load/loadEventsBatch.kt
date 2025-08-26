package de.benkralex.socius.data.contacts.system.load

import android.content.ContentResolver
import android.provider.ContactsContract
import de.benkralex.socius.data.ContactEvent

fun loadEventsBatch(contentResolver: ContentResolver, contactIds: List<String>): Map<String, List<ContactEvent>> {
    val result = mutableMapOf<String, MutableList<ContactEvent>>()
    if (contactIds.isEmpty()) return result
    val selection = "${ContactsContract.Data.CONTACT_ID} IN (${contactIds.joinToString(",")}) AND ${ContactsContract.Data.MIMETYPE} = ?"
    val selectionArgs = arrayOf(ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)
    val cursor = contentResolver.query(
        ContactsContract.Data.CONTENT_URI,
        null,
        selection,
        selectionArgs,
        null
    )
    cursor?.use {
        val contactIdIndex = it.getColumnIndex(ContactsContract.Data.CONTACT_ID)
        val startDateIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE)
        val typeIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Event.TYPE)
        val labelIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Event.LABEL)
        while (it.moveToNext()) {
            val contactId = it.getString(contactIdIndex)
            val startDate = if (startDateIndex != -1) it.getString(startDateIndex) else null
            val label = if (labelIndex != -1) it.getString(labelIndex) else null
            val type = if (typeIndex != -1) it.getInt(typeIndex) else 0
            val typeStr = when (type) {
                ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY -> "birthday"
                ContactsContract.CommonDataKinds.Event.TYPE_ANNIVERSARY -> "anniversary"
                ContactsContract.CommonDataKinds.Event.TYPE_OTHER -> "other"
                ContactsContract.CommonDataKinds.Event.TYPE_CUSTOM -> "custom"
                else -> "other"
            }
            //formats: YYYY-MM-DD, YYYYMMDD, --MM-DD, or MM DD
            var year: Int? = null
            var month: Int? = null
            var day: Int? = null
            if (startDate != null) {
                val date = startDate.replace("-", "")
                when (date.length) {
                    8 -> {
                        year = date.substring(0, 4).toIntOrNull()
                        month = date.substring(4, 6).toIntOrNull()
                        day = date.substring(6, 8).toIntOrNull()
                    }
                    4 -> {
                        month = date.substring(0, 2).toIntOrNull()
                        day = date.substring(2, 4).toIntOrNull()
                    }
                    else -> {
                        throw IllegalArgumentException("Invalid date format: $startDate")
                    }
                }
            }
            if (startDate != null) {
                val event = ContactEvent(
                    day = day,
                    month = month,
                    year = year,
                    type = typeStr,
                    label = label,
                )
                val eventsForContact = result.getOrPut(contactId) { mutableListOf() }
                if (!eventsForContact.contains(event)) {
                    eventsForContact.add(event)
                }
            }
        }
    }
    return result
}