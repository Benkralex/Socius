package de.benkralex.contacts.backend

import android.content.ContentResolver
import android.content.Context
import android.graphics.BitmapFactory
import android.provider.ContactsContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.core.net.toUri

suspend fun getAndroidSystemContacts(context: Context): List<Contact> =
    withContext(Dispatchers.IO) {
        val contacts = mutableListOf<Contact>()
        val contentResolver = context.contentResolver

        // Only query required columns (projection)
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

            // Collect contact IDs for batch queries
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

                contacts.add(Contact(
                    id = contactId,
                    displayName = displayName,
                    photoUri = photoUri,
                    photoBitmap = photoBitmap,
                    thumbnailUri = thumbnailUri,
                    thumbnailBitmap = thumbnailBitmap
                ))
            }

            // Perform batch queries for all contacts
            if (contactIds.isNotEmpty()) {
                val structuredNames = loadStructuredNamesBatch(contentResolver, contactIds)
                val nicknames = getNicknamesBatch(contentResolver, contactIds)
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

                // Assign data to the corresponding contacts
                contacts.forEach { contact ->
                    contact.id?.let { id ->
                        // Structured Names
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

                        // Nickname
                        contact.nickname = nicknames[id]

                        // Phone numbers
                        contact.phoneNumbers = phoneNumbers[id] ?: emptyList()

                        // E-Mails
                        contact.emails = emails[id] ?: emptyList()

                        // Addresses
                        contact.addresses = addresses[id] ?: emptyList()

                        // Organisations
                        organizations[id]?.let { org ->
                            contact.organization = org.organization
                            contact.department = org.department
                            contact.jobTitle = org.jobTitle
                        }

                        // Notes
                        contact.note = notes[id]

                        // Websites
                        contact.websites = websites[id] ?: emptyList()

                        // Events
                        contact.events = events[id] ?: emptyList()

                        // Relations
                        contact.relations = relations[id] ?: emptyList()

                        // Groups
                        contact.groups = groups[id] ?: emptyList()

                        // Is Starred
                        contact.isStarred = (groups[id] ?: emptyList())
                            .any { it.name?.contains("Starred", ignoreCase = true) ?: false }

                        // Custom Fields
                        contact.customFields = customFields[id] ?: emptyMap()
                    }
                }
            }
        }

        // Filter out empty contacts
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

// Helper class for structured names
private data class StructuredNameData(
    val prefix: String? = null,
    val givenName: String? = null,
    val middleName: String? = null,
    val familyName: String? = null,
    val suffix: String? = null,
    val phoneticGivenName: String? = null,
    val phoneticMiddleName: String? = null,
    val phoneticFamilyName: String? = null
)

// Helper class for organization data
private data class OrganizationData(
    val organization: String? = null,
    val department: String? = null,
    val jobTitle: String? = null
)

// Batch query for structured names
private fun loadStructuredNamesBatch(contentResolver: ContentResolver, contactIds: List<String>): Map<String, StructuredNameData> {
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

private fun getNicknamesBatch(contentResolver: ContentResolver, contactIds: List<String>): Map<String, String?> {
    val result = mutableMapOf<String, String?>()

    if (contactIds.isEmpty()) return result

    val selection = "${ContactsContract.Data.CONTACT_ID} IN (${contactIds.joinToString(",")}) AND ${ContactsContract.Data.MIMETYPE} = ?"
    val selectionArgs = arrayOf(ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE)

    val cursor = contentResolver.query(
        ContactsContract.Data.CONTENT_URI,
        null,
        selection,
        selectionArgs,
        null
    )

    cursor?.use {
        val contactIdIndex = it.getColumnIndex(ContactsContract.Data.CONTACT_ID)
        val nicknameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Nickname.NAME)

        while (it.moveToNext()) {
            val contactId = it.getString(contactIdIndex)
            val nickname = if (nicknameIndex != -1) it.getString(nicknameIndex) else null
            result[contactId] = nickname
        }
    }

    return result
}

// Batch query for phone numbers
private fun loadPhoneNumbersBatch(contentResolver: ContentResolver, contactIds: List<String>): Map<String, List<PhoneNumber>> {
    val result = mutableMapOf<String, MutableList<PhoneNumber>>()

    if (contactIds.isEmpty()) return result

    val selection = "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} IN (${contactIds.joinToString(",")})"

    val cursor = contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null,
        selection,
        null,
        null
    )

    cursor?.use { item ->
        val contactIdIndex = item.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
        val numberIndex = item.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
        val typeIndex = item.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)
        val labelIndex = item.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL)

        while (item.moveToNext()) {
            val contactId = item.getString(contactIdIndex)
            val number = if (numberIndex != -1) item.getString(numberIndex) else null
            val type = if (typeIndex != -1) item.getInt(typeIndex) else 0
            val label = if (labelIndex != -1) item.getString(labelIndex) else null

            val typeStr = when (type) {
                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> "mobile"
                ContactsContract.CommonDataKinds.Phone.TYPE_HOME -> "home"
                ContactsContract.CommonDataKinds.Phone.TYPE_WORK -> "work"
                ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME -> "fax_home"
                ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK -> "fax_work"
                ContactsContract.CommonDataKinds.Phone.TYPE_PAGER -> "pager"
                ContactsContract.CommonDataKinds.Phone.TYPE_OTHER -> "other"
                ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM -> "custom"
                ContactsContract.CommonDataKinds.Phone.TYPE_ASSISTANT -> "assistant"
                ContactsContract.CommonDataKinds.Phone.TYPE_CALLBACK -> "callback"
                ContactsContract.CommonDataKinds.Phone.TYPE_CAR -> "car"
                ContactsContract.CommonDataKinds.Phone.TYPE_COMPANY_MAIN -> "company_main"
                ContactsContract.CommonDataKinds.Phone.TYPE_ISDN -> "isdn"
                ContactsContract.CommonDataKinds.Phone.TYPE_MAIN -> "main"
                ContactsContract.CommonDataKinds.Phone.TYPE_MMS -> "mms"
                ContactsContract.CommonDataKinds.Phone.TYPE_OTHER_FAX -> "fax_other"
                ContactsContract.CommonDataKinds.Phone.TYPE_RADIO -> "radio"
                ContactsContract.CommonDataKinds.Phone.TYPE_TELEX -> "telex"
                ContactsContract.CommonDataKinds.Phone.TYPE_TTY_TDD -> "tty_tdd"
                ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE -> "work_mobile"
                ContactsContract.CommonDataKinds.Phone.TYPE_WORK_PAGER -> "work_pager"
                else -> "unknown"
            }

            val phoneNumbersForContact = result.getOrPut(contactId) { mutableListOf() }
            val isDuplicate = phoneNumbersForContact.any { it.number.replace(" ", "") == number?.replace(" ", "") }

            if (!isDuplicate && number != null) {
                phoneNumbersForContact.add(PhoneNumber(number, typeStr, label))
            }
        }
    }

    return result
}

// More batch query methods...
// (Implementation of the other methods similar to above)

private fun loadEmailsBatch(contentResolver: ContentResolver, contactIds: List<String>): Map<String, List<Email>> {
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

private fun loadAddressesBatch(contentResolver: ContentResolver, contactIds: List<String>): Map<String, List<PostalAddress>> {
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

private fun loadOrganizationsBatch(contentResolver: ContentResolver, contactIds: List<String>): Map<String, OrganizationData> {
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

private fun loadNotesBatch(contentResolver: ContentResolver, contactIds: List<String>): Map<String, String?> {
    val result = mutableMapOf<String, String?>()
    if (contactIds.isEmpty()) return result
    val selection = "${ContactsContract.Data.CONTACT_ID} IN (${contactIds.joinToString(",")}) AND ${ContactsContract.Data.MIMETYPE} = ?"
    val selectionArgs = arrayOf(ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)
    val cursor = contentResolver.query(
        ContactsContract.Data.CONTENT_URI,
        null,
        selection,
        selectionArgs,
        null
    )
    cursor?.use {
        val contactIdIndex = it.getColumnIndex(ContactsContract.Data.CONTACT_ID)
        val noteIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE)
        while (it.moveToNext()) {
            val contactId = it.getString(contactIdIndex)
            val note = if (noteIndex != -1) it.getString(noteIndex) else null
            result[contactId] = note
        }
    }
    return result
}

private fun loadWebsitesBatch(contentResolver: ContentResolver, contactIds: List<String>): Map<String, List<Website>> {
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

private fun loadEventsBatch(contentResolver: ContentResolver, contactIds: List<String>): Map<String, List<ContactEvent>> {
    val result = mutableMapOf<String, MutableList<ContactEvent>>()
    if (contactIds.isEmpty()) return result
    val selection = "${ContactsContract.Data.CONTACT_ID} IN (${contactIds.joinToString(",")}) AND ${ContactsContract.Data.MIMETYPE} = ?"
    val selectionArgs = arrayOf(ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)
    val cursor = contentResolver.query(
        ContactsContract.Data.CONTENT_URI,
        null,
        selection,
        selectionArgs,
        null
    )
    cursor?.use {
        val contactIdIndex = it.getColumnIndex(ContactsContract.Data.CONTACT_ID)
        val startDateIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE)
        val typeIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Event.TYPE)
        val labelIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Event.LABEL)
        while (it.moveToNext()) {
            val contactId = it.getString(contactIdIndex)
            val startDate = if (startDateIndex != -1) it.getString(startDateIndex) else null
            val label = if (labelIndex != -1) it.getString(labelIndex) else null
            val type = if (typeIndex != -1) it.getInt(typeIndex) else 0
            val typeStr = when (type) {
                ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY -> "birthday"
                ContactsContract.CommonDataKinds.Event.TYPE_ANNIVERSARY -> "anniversary"
                ContactsContract.CommonDataKinds.Event.TYPE_OTHER -> "other"
                ContactsContract.CommonDataKinds.Event.TYPE_CUSTOM -> "custom"
                else -> "other"
            }
            //formats: YYYY-MM-DD, YYYYMMDD, --MM-DD, or MM DD
            var year: Int? = null
            var month: Int? = null
            var day: Int? = null
            if (startDate != null) {
                val date = startDate.replace("-", "")
                when (date.length) {
                    8 -> {
                        year = date.substring(0, 4).toIntOrNull()
                        month = date.substring(4, 6).toIntOrNull()
                        day = date.substring(6, 8).toIntOrNull()
                    }
                    4 -> {
                        month = date.substring(0, 2).toIntOrNull()
                        day = date.substring(2, 4).toIntOrNull()
                    }
                    else -> {
                        throw IllegalArgumentException("Invalid date format: $startDate")
                    }
                }
            }
            if (startDate != null) {
                val event = ContactEvent(
                    day = day,
                    month = month,
                    year = year,
                    type = typeStr,
                    label = label,
                )
                if (result[contactId] == null) result[contactId] = mutableListOf()
                result[contactId]?.add(event)
            }
        }
    }
    return result
}

private fun loadRelationsBatch(contentResolver: ContentResolver, contactIds: List<String>): Map<String, List<Relation>> {
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

private fun loadGroupsBatch(contentResolver: ContentResolver, contactIds: List<String>): Map<String, List<Group>> {
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

private fun loadCustomFieldsBatch(contentResolver: ContentResolver, contactIds: List<String>): Map<String, Map<String, String>> {
    val result = mutableMapOf<String, MutableMap<String, String>>()
    if (contactIds.isEmpty()) return result
    val selection = "${ContactsContract.Data.CONTACT_ID} IN (${contactIds.joinToString(",")}) AND ${ContactsContract.Data.MIMETYPE} = ?"
    val selectionArgs = arrayOf(ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)
    val cursor = contentResolver.query(
        ContactsContract.Data.CONTENT_URI,
        null,
        selection,
        selectionArgs,
        null
    )
    cursor?.use {
        val contactIdIndex = it.getColumnIndex(ContactsContract.Data.CONTACT_ID)
        val keyIndex = it.getColumnIndex(ContactsContract.Data.DATA1)
        val valueIndex = it.getColumnIndex(ContactsContract.Data.DATA2)
        while (it.moveToNext()) {
            val contactId = it.getString(contactIdIndex)
            val key = if (keyIndex != -1) it.getString(keyIndex) else null
            val value = if (valueIndex != -1) it.getString(valueIndex) else null
            if (key != null && value != null) {
                if (result[contactId] == null) result[contactId] = mutableMapOf()
                result[contactId]?.put(key, value)
            }
        }
    }
    return result
}