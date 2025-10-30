package de.benkralex.socius.data.contacts.system.load

import android.content.Context
import android.graphics.BitmapFactory
import android.provider.ContactsContract
import androidx.core.net.toUri
import de.benkralex.socius.data.Contact
import de.benkralex.socius.data.ContactOrigin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun getAndroidSystemContacts(context: Context): List<Contact> =
    withContext(Dispatchers.IO) {
        val contacts = mutableListOf<Contact>()
        val contentResolver = context.contentResolver

        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.PHOTO_URI,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
        )

        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            projection,
            null,
            null,
            "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} ASC"
        )

        cursor?.use { item ->
            val idIndex = item.getColumnIndex(ContactsContract.Contacts._ID)
            val displayNameIndex = item.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
            val photoUriIndex = item.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)
            val thumbnailUriIndex = item.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)

            val contactIds = mutableListOf<String>()

            while (item.moveToNext()) {
                val contactId = item.getString(idIndex)
                contactIds.add(contactId)


                val displayName = item.getString(displayNameIndex)
                val photoUri = if (photoUriIndex != -1) item.getString(photoUriIndex) else null
                val thumbnailUri = if (thumbnailUriIndex != -1) item.getString(thumbnailUriIndex) else null


                val photoBitmap = if (photoUri != null) {
                    try {
                        contentResolver.openInputStream(photoUri.toUri())?.use { inputStream ->
                            BitmapFactory.decodeStream(inputStream)
                        }
                    } catch (_: Exception) {
                        null
                    }
                } else null

                val thumbnailBitmap = if (thumbnailUri != null) {
                    try {
                        contentResolver.openInputStream(thumbnailUri.toUri())?.use { inputStream ->
                        BitmapFactory.decodeStream(inputStream)
                        }
                    } catch (_: Exception) {
                        null
                    }
                } else null

                contacts.add(
                    Contact(
                        id = contactId,
                        origin = ContactOrigin.SYSTEM,
                        displayName = displayName,
                        photoUri = photoUri,
                        photoBitmap = photoBitmap,
                        thumbnailUri = thumbnailUri,
                        thumbnailBitmap = thumbnailBitmap
                    )
                )
            }

            if (contactIds.isNotEmpty()) {
                val structuredNames = loadStructuredNamesBatch(contentResolver, contactIds)
                val nicknames = loadNicknamesBatch(contentResolver, contactIds)
                val phoneNumbers = loadPhoneNumbersBatch(contentResolver, contactIds)
                val emails = loadEmailsBatch(contentResolver, contactIds)
                val addresses = loadAddressesBatch(contentResolver, contactIds)
                val organizations = loadOrganizationsBatch(contentResolver, contactIds)
                val notes = loadNotesBatch(contentResolver, contactIds)
                val websites = loadWebsitesBatch(contentResolver, contactIds)
                val events = loadEventsBatch(contentResolver, contactIds)
                val relations = loadRelationsBatch(contentResolver, contactIds)
                val groups = loadGroupsBatch(contentResolver, contactIds)
                val customFields = loadCustomFieldsBatch(contentResolver, contactIds)
                val isStarred = loadStarredBatch(contentResolver, contactIds)

                contacts.forEach { contact ->
                    contact.id.let { id ->
                        structuredNames[id]?.let { structuredName ->
                            contact.prefix = structuredName.prefix
                            contact.givenName = structuredName.givenName
                            contact.middleName = structuredName.middleName
                            contact.familyName = structuredName.familyName
                            contact.suffix = structuredName.suffix
                            contact.phoneticGivenName = structuredName.phoneticGivenName
                            contact.phoneticMiddleName = structuredName.phoneticMiddleName
                            contact.phoneticFamilyName = structuredName.phoneticFamilyName
                        }
                        contact.nickname = nicknames[id]
                        contact.phoneNumbers = phoneNumbers[id] ?: emptyList()
                        contact.emails = emails[id] ?: emptyList()
                        contact.addresses = addresses[id] ?: emptyList()
                        organizations[id]?.let { org ->
                            contact.organization = org.organization
                            contact.department = org.department
                            contact.jobTitle = org.jobTitle
                        }
                        contact.note = notes[id]
                        contact.websites = websites[id] ?: emptyList()
                        contact.events = events[id] ?: emptyList()
                        contact.relations = relations[id] ?: emptyList()
                        contact.groups = groups[id] ?: emptyList()
                        contact.isStarred = isStarred[id] ?: false
                        contact.customFields = customFields[id] ?: emptyMap()
                    }
                }
            }
        }

        contacts.filter { contact ->
            !contact.displayName.isNullOrBlank() || hasRelevantData(contact)
        }
    }

private fun hasRelevantData(contact: Contact): Boolean {
    return contact.phoneNumbers.isNotEmpty() ||
            contact.emails.isNotEmpty() ||
            contact.addresses.isNotEmpty() ||
            contact.organization != null ||
            contact.note != null
}