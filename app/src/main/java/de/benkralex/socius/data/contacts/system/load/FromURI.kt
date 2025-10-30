package de.benkralex.socius.data.contacts.system.load

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.ContactsContract
import androidx.core.net.toUri
import de.benkralex.socius.data.Contact
import de.benkralex.socius.data.ContactOrigin
import de.benkralex.socius.data.contacts.system.data.StructuredNameData

fun loadFromURI(context: Context, uri: Uri): Contact {
    val contentResolver = context.contentResolver
        val cursor = contentResolver.query(
            uri,
            arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                ContactsContract.Contacts.PHOTO_URI,
                ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
            ),
            null,
            null,
            null
        )

        cursor?.use { item ->
            if (item.moveToFirst()) {
                val idIndex = item.getColumnIndex(ContactsContract.Contacts._ID)
                val displayNameIndex = item.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
                val photoUriIndex = item.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)
                val thumbnailUriIndex = item.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)

                val contactId = item.getString(idIndex)
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

                val structuredName = loadStructuredNamesBatch(contentResolver, listOf(contactId))[contactId] ?: StructuredNameData("", "", "", "", "", "", "", "")
                val nickname = loadNicknamesBatch(contentResolver, listOf(contactId))[contactId]
                val phoneNumbers = loadPhoneNumbersBatch(contentResolver, listOf(contactId))[contactId] ?: emptyList()
                val emails = loadEmailsBatch(contentResolver, listOf(contactId))[contactId] ?: emptyList()
                val addresses = loadAddressesBatch(contentResolver, listOf(contactId))[contactId] ?: emptyList()
                val organization = loadOrganizationsBatch(contentResolver, listOf(contactId))[contactId]
                val note = loadNotesBatch(contentResolver, listOf(contactId))[contactId]
                val websites = loadWebsitesBatch(contentResolver, listOf(contactId))[contactId] ?: emptyList()
                val events = loadEventsBatch(contentResolver, listOf(contactId))[contactId] ?: emptyList()
                val relations = loadRelationsBatch(contentResolver, listOf(contactId))[contactId] ?: emptyList()
                val groups = loadGroupsBatch(contentResolver, listOf(contactId))[contactId] ?: emptyList()
                val customFields = loadCustomFieldsBatch(contentResolver, listOf(contactId))[contactId] ?: emptyMap()
                val isStarred = loadStarredBatch(contentResolver, listOf(contactId))[contactId] == true
                
                return Contact(
                    id = contactId,
                    origin = ContactOrigin.URI,
                    displayName = displayName,
                    photoUri = photoUri,
                    photoBitmap = photoBitmap,
                    thumbnailUri = thumbnailUri,
                    thumbnailBitmap = thumbnailBitmap,
                    prefix = structuredName.prefix,
                    givenName = structuredName.givenName,
                    middleName = structuredName.middleName,
                    familyName = structuredName.familyName,
                    suffix = structuredName.suffix,
                    phoneticGivenName = structuredName.phoneticGivenName,
                    phoneticMiddleName = structuredName.phoneticMiddleName,
                    phoneticFamilyName = structuredName.phoneticFamilyName,
                    nickname = nickname,
                    phoneNumbers = phoneNumbers,
                    emails = emails,
                    addresses = addresses,
                    organization = organization?.organization,
                    jobTitle = organization?.jobTitle,
                    department = organization?.department,
                    note = note,
                    websites = websites,
                    events = events,
                    relations = relations,
                    groups = groups,
                    customFields = customFields,
                    isStarred = isStarred,
                )
            }
        }
        throw IllegalArgumentException("Kein Kontakt für die gegebene URI gefunden")
}