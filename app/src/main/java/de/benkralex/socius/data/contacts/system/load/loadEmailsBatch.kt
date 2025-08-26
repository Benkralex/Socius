package de.benkralex.socius.data.contacts.system.load

import android.content.ContentResolver
import android.provider.ContactsContract
import de.benkralex.socius.data.Email

fun loadEmailsBatch(contentResolver: ContentResolver, contactIds: List<String>): Map<String, List<Email>> {
    val result = mutableMapOf<String, MutableList<Email>>()

    if (contactIds.isEmpty()) return result

    val selection = "${ContactsContract.CommonDataKinds.Email.CONTACT_ID} IN (${contactIds.joinToString(",")})"

    val cursor = contentResolver.query(
        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
        null,
        selection,
        null,
        null
    )

    cursor?.use {
        val contactIdIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID)
        val addressIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
        val typeIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE)
        val labelIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Email.LABEL)

        while (it.moveToNext()) {
            val contactId = it.getString(contactIdIndex)
            val address = if (addressIndex != -1) it.getString(addressIndex) else null
            val type = if (typeIndex != -1) it.getInt(typeIndex) else 0
            val label = if (labelIndex != -1) it.getString(labelIndex) else null

            val typeStr = when (type) {
                ContactsContract.CommonDataKinds.Email.TYPE_HOME -> "home"
                ContactsContract.CommonDataKinds.Email.TYPE_WORK -> "work"
                ContactsContract.CommonDataKinds.Email.TYPE_OTHER -> "other"
                ContactsContract.CommonDataKinds.Email.TYPE_MOBILE -> "mobile"
                ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM -> "custom"
                else -> "unknown"
            }

            if (!result.containsKey(contactId)) {
                result[contactId] = mutableListOf()
            }

            result[contactId]?.add(Email(address ?: "", typeStr, label))
        }
    }

    return result
}