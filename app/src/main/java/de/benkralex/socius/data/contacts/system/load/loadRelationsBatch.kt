package de.benkralex.socius.data.contacts.system.load

import android.content.ContentResolver
import android.provider.ContactsContract
import de.benkralex.socius.data.Relation

fun loadRelationsBatch(contentResolver: ContentResolver, contactIds: List<String>): Map<String, List<Relation>> {
    val result = mutableMapOf<String, MutableList<Relation>>()
    if (contactIds.isEmpty()) return result
    val selection = "${ContactsContract.Data.CONTACT_ID} IN (${contactIds.joinToString(",")}) AND ${ContactsContract.Data.MIMETYPE} = ?"
    val selectionArgs = arrayOf(ContactsContract.CommonDataKinds.Relation.CONTENT_ITEM_TYPE)
    val cursor = contentResolver.query(
        ContactsContract.Data.CONTENT_URI,
        null,
        selection,
        selectionArgs,
        null
    )
    cursor?.use {
        val contactIdIndex = it.getColumnIndex(ContactsContract.Data.CONTACT_ID)
        val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Relation.NAME)
        val typeIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Relation.TYPE)
        val labelIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Relation.LABEL)
        while (it.moveToNext()) {
            val contactId = it.getString(contactIdIndex)
            val name = if (nameIndex != -1) it.getString(nameIndex) else null
            val label = if (labelIndex != -1) it.getString(labelIndex) else null
            val type = if (typeIndex != -1) it.getInt(typeIndex) else 0
            val typeStr = when (type) {
                ContactsContract.CommonDataKinds.Relation.TYPE_CUSTOM -> "custom"
                ContactsContract.CommonDataKinds.Relation.TYPE_ASSISTANT -> "assistant"
                ContactsContract.CommonDataKinds.Relation.TYPE_BROTHER -> "brother"
                ContactsContract.CommonDataKinds.Relation.TYPE_CHILD -> "child"
                ContactsContract.CommonDataKinds.Relation.TYPE_DOMESTIC_PARTNER -> "domestic_partner"
                ContactsContract.CommonDataKinds.Relation.TYPE_FATHER -> "father"
                ContactsContract.CommonDataKinds.Relation.TYPE_FRIEND -> "friend"
                ContactsContract.CommonDataKinds.Relation.TYPE_MANAGER -> "manager"
                ContactsContract.CommonDataKinds.Relation.TYPE_MOTHER -> "mother"
                ContactsContract.CommonDataKinds.Relation.TYPE_PARENT -> "parent"
                ContactsContract.CommonDataKinds.Relation.TYPE_PARTNER -> "partner"
                ContactsContract.CommonDataKinds.Relation.TYPE_REFERRED_BY -> "referred_by"
                ContactsContract.CommonDataKinds.Relation.TYPE_RELATIVE -> "relative"
                ContactsContract.CommonDataKinds.Relation.TYPE_SISTER -> "sister"
                ContactsContract.CommonDataKinds.Relation.TYPE_SPOUSE -> "spouse"
                else -> "other"
            }
            if (name != null) {
                val relation = Relation(name, typeStr, label)
                if (result[contactId] == null) result[contactId] = mutableListOf()
                result[contactId]?.add(relation)
            }
        }
    }
    return result
}