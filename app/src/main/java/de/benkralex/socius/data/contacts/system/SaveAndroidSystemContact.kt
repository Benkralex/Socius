package de.benkralex.socius.data.contacts.system

import android.content.ContentProviderOperation
import android.content.Context
import android.provider.ContactsContract


fun updateStarred(contactId: String?, value: Boolean, context: Context) {
    if (contactId == null) return
    val operations = ArrayList<ContentProviderOperation>()
    operations.add(
        ContentProviderOperation.newUpdate(ContactsContract.RawContacts.CONTENT_URI)
            .withSelection("${ContactsContract.RawContacts._ID} = ?", arrayOf(contactId))
            .withValue(ContactsContract.RawContacts.STARRED, if (value) 1 else 0)
            .build()
    )
    if (value) {
        operations.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValue(ContactsContract.Data.RAW_CONTACT_ID, contactId)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE)
                .withValue(
                    ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID,
                    "Starred in Android"
                )
                .build()
        )
    } else {
        operations.add(
            ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
                .withSelection(
                    "${ContactsContract.Data.RAW_CONTACT_ID}=? AND ${ContactsContract.Data.MIMETYPE}=? AND ${ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID}=?",
                    arrayOf(
                        contactId,
                        ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE,
                        "Starred in Android",
                    )
                )
                .build()
        )
    }
    val contentResolver = context.contentResolver
    contentResolver.applyBatch(ContactsContract.AUTHORITY, operations)
}