package de.benkralex.socius.data.contacts.system.load

import android.content.ContentResolver
import android.provider.ContactsContract
import de.benkralex.socius.data.Group

fun loadGroupsBatch(contentResolver: ContentResolver, contactIds: List<String>): Map<String, List<Group>> {
    val result = mutableMapOf<String, MutableList<Group>>()
    if (contactIds.isEmpty()) return result
    val selection = "${ContactsContract.Data.CONTACT_ID} IN (${contactIds.joinToString(",")}) AND ${ContactsContract.Data.MIMETYPE} = ?"
    val selectionArgs = arrayOf(ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE)
    val cursor = contentResolver.query(
        ContactsContract.Data.CONTENT_URI,
        null,
        selection,
        selectionArgs,
        null
    )
    cursor?.use {
        val contactIdIndex = it.getColumnIndex(ContactsContract.Data.CONTACT_ID)
        val groupIdIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID)
        val groupNameCache = mutableMapOf<Long, String>()
        while (it.moveToNext()) {
            val contactId = it.getString(contactIdIndex)
            val groupId = it.getLong(groupIdIndex)
            if (groupId != 0L) {
                if (result[contactId] == null) result[contactId] = mutableListOf()
                val groupName = groupNameCache.getOrPut(groupId) {
                    contentResolver.query(
                        ContactsContract.Groups.CONTENT_URI,
                        arrayOf(ContactsContract.Groups.TITLE),
                        "${ContactsContract.Groups._ID} = ?",
                        arrayOf(groupId.toString()),
                        null
                    )?.use { groupCursor ->
                        if (groupCursor.moveToFirst()) {
                            val titleIndex = groupCursor.getColumnIndex(ContactsContract.Groups.TITLE)
                            if (titleIndex != -1) groupCursor.getString(titleIndex) else null
                        } else null
                    } ?: "Unknown Group"
                }
                result[contactId]?.add(
                    Group(
                        id = groupId,
                        name = groupName,
                    )
                )
            }
        }
    }
    return result
}