package de.benkralex.socius.data.contacts.system.load

import android.content.ContentResolver
import android.provider.ContactsContract
import de.benkralex.socius.data.Website

fun loadWebsitesBatch(contentResolver: ContentResolver, contactIds: List<String>): Map<String, List<Website>> {
    val result = mutableMapOf<String, MutableList<Website>>()
    if (contactIds.isEmpty()) return result
    val selection = "${ContactsContract.Data.CONTACT_ID} IN (${contactIds.joinToString(",")}) AND ${ContactsContract.Data.MIMETYPE} = ?"
    val selectionArgs = arrayOf(ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE)
    val cursor = contentResolver.query(
        ContactsContract.Data.CONTENT_URI,
        null,
        selection,
        selectionArgs,
        null
    )
    cursor?.use {
        val contactIdIndex = it.getColumnIndex(ContactsContract.Data.CONTACT_ID)
        val urlIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Website.URL)
        val typeIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Website.TYPE)
        val labelIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Website.LABEL)
        while (it.moveToNext()) {
            val contactId = it.getString(contactIdIndex)
            var url = if (urlIndex != -1) it.getString(urlIndex) else null
            if (!(url?.startsWith("http://") == true || url?.startsWith("https://") == true) && url != null) {
                url = "http://$url"
            }
            val label = if (labelIndex != -1) it.getString(labelIndex) else null
            val type = if (typeIndex != -1) it.getInt(typeIndex) else 0
            val typeStr = when (type) {
                ContactsContract.CommonDataKinds.Website.TYPE_HOME -> "homepage"
                ContactsContract.CommonDataKinds.Website.TYPE_WORK -> "work"
                ContactsContract.CommonDataKinds.Website.TYPE_BLOG -> "blog"
                ContactsContract.CommonDataKinds.Website.TYPE_PROFILE -> "profile"
                ContactsContract.CommonDataKinds.Website.TYPE_FTP -> "ftp"
                ContactsContract.CommonDataKinds.Website.TYPE_CUSTOM -> "custom"
                else -> "homepage"
            }
            if (url != null) {
                val website = Website(url, typeStr, label)
                if (result[contactId] == null) result[contactId] = mutableListOf()
                result[contactId]?.add(website)
            }
        }
    }
    return result
}