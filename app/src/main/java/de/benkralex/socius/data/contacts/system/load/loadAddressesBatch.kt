package de.benkralex.socius.data.contacts.system.load

import android.content.ContentResolver
import android.provider.ContactsContract
import de.benkralex.socius.data.PostalAddress

fun loadAddressesBatch(contentResolver: ContentResolver, contactIds: List<String>): Map<String, List<PostalAddress>> {
    val result = mutableMapOf<String, MutableList<PostalAddress>>()
    if (contactIds.isEmpty()) return result
    val selection = "${ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID} IN (${contactIds.joinToString(",")})"
    val cursor = contentResolver.query(
        ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
        null,
        selection,
        null,
        null
    )
    cursor?.use {
        val contactIdIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID)
        val streetIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET)
        val cityIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY)
        val regionIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION)
        val postalCodeIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE)
        val countryIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY)
        val typeIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE)
        val labelIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.LABEL)
        while (it.moveToNext()) {
            val contactId = it.getString(contactIdIndex)
            val type = if (typeIndex != -1) it.getInt(typeIndex) else 0
            val label = if (labelIndex != -1) it.getString(labelIndex) else null
            val typeStr = when (type) {
                ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME -> "home"
                ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK -> "work"
                ContactsContract.CommonDataKinds.StructuredPostal.TYPE_OTHER -> "other"
                ContactsContract.CommonDataKinds.StructuredPostal.TYPE_CUSTOM -> "custom"
                else -> "unknown"
            }
            val address = PostalAddress(
                street = if (streetIndex != -1) it.getString(streetIndex) else null,
                city = if (cityIndex != -1) it.getString(cityIndex) else null,
                region = if (regionIndex != -1) it.getString(regionIndex) else null,
                postcode = if (postalCodeIndex != -1) it.getString(postalCodeIndex) else null,
                country = if (countryIndex != -1) it.getString(countryIndex) else null,
                type = typeStr,
                label = label
            )
            if (result[contactId] == null) result[contactId] = mutableListOf()
            result[contactId]?.add(address)
        }
    }
    return result
}