package de.benkralex.socius.data.contacts.system.load

import android.content.ContentResolver
import android.provider.ContactsContract
import de.benkralex.socius.data.contacts.system.data.StructuredNameData

fun loadStructuredNamesBatch(contentResolver: ContentResolver, contactIds: List<String>): Map<String, StructuredNameData> {
    val result = mutableMapOf<String, StructuredNameData>()

    if (contactIds.isEmpty()) return result

    val selection = "${ContactsContract.Data.CONTACT_ID} IN (${contactIds.joinToString(",")}) AND ${ContactsContract.Data.MIMETYPE} = ?"
    val selectionArgs = arrayOf(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)

    val cursor = contentResolver.query(
        ContactsContract.Data.CONTENT_URI,
        null,
        selection,
        selectionArgs,
        null
    )

    cursor?.use {
        val contactIdIndex = it.getColumnIndex(ContactsContract.Data.CONTACT_ID)
        val prefixIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PREFIX)
        val givenNameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME)
        val middleNameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME)
        val familyNameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME)
        val suffixIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.SUFFIX)
        val phoneticGivenNameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_GIVEN_NAME)
        val phoneticMiddleNameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_MIDDLE_NAME)
        val phoneticFamilyNameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_FAMILY_NAME)

        while (it.moveToNext()) {
            val contactId = it.getString(contactIdIndex)

            result[contactId] = StructuredNameData(
                prefix = if (prefixIndex != -1) it.getString(prefixIndex) else null,
                givenName = if (givenNameIndex != -1) it.getString(givenNameIndex) else null,
                middleName = if (middleNameIndex != -1) it.getString(middleNameIndex) else null,
                familyName = if (familyNameIndex != -1) it.getString(familyNameIndex) else null,
                suffix = if (suffixIndex != -1) it.getString(suffixIndex) else null,
                phoneticGivenName = if (phoneticGivenNameIndex != -1) it.getString(phoneticGivenNameIndex) else null,
                phoneticMiddleName = if (phoneticMiddleNameIndex != -1) it.getString(phoneticMiddleNameIndex) else null,
                phoneticFamilyName = if (phoneticFamilyNameIndex != -1) it.getString(phoneticFamilyNameIndex) else null
            )
        }
    }

    return result
}