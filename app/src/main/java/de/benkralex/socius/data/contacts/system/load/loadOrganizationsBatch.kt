package de.benkralex.socius.data.contacts.system.load

import android.content.ContentResolver
import android.provider.ContactsContract
import de.benkralex.socius.data.contacts.system.data.OrganizationData

fun loadOrganizationsBatch(contentResolver: ContentResolver, contactIds: List<String>): Map<String, OrganizationData> {
    val result = mutableMapOf<String, OrganizationData>()
    if (contactIds.isEmpty()) return result
    val selection = "${ContactsContract.Data.CONTACT_ID} IN (${contactIds.joinToString(",")}) AND ${ContactsContract.Data.MIMETYPE} = ?"
    val selectionArgs = arrayOf(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
    val cursor = contentResolver.query(
        ContactsContract.Data.CONTENT_URI,
        null,
        selection,
        selectionArgs,
        null
    )
    cursor?.use {
        val contactIdIndex = it.getColumnIndex(ContactsContract.Data.CONTACT_ID)
        val organizationIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY)
        val departmentIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DEPARTMENT)
        val jobTitleIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE)
        while (it.moveToNext()) {
            val contactId = it.getString(contactIdIndex)
            val organization = if (organizationIndex != -1) it.getString(organizationIndex) else null
            val department = if (departmentIndex != -1) it.getString(departmentIndex) else null
            val jobTitle = if (jobTitleIndex != -1) it.getString(jobTitleIndex) else null
            result[contactId] = OrganizationData(
                organization = organization,
                department = department,
                jobTitle = jobTitle
            )
        }
    }
    return result
}