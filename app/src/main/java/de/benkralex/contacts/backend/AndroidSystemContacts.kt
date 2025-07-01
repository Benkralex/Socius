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

        // Nur benötigte Spalten abfragen (Projektion)
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

        cursor?.use {
            val idIndex = it.getColumnIndex(ContactsContract.Contacts._ID)
            val displayNameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
            val photoUriIndex = it.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)
            val thumbnailUriIndex = it.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)

            // Kontakt-IDs sammeln für Batch-Abfragen
            val contactIds = mutableListOf<String>()

            while (it.moveToNext()) {
                val contactId = it.getString(idIndex)
                contactIds.add(contactId)

                val displayName = it.getString(displayNameIndex)
                val photoUri = if (photoUriIndex != -1) it.getString(photoUriIndex) else null
                val thumbnailUri = if (thumbnailUriIndex != -1) it.getString(thumbnailUriIndex) else null

                val photoBitmap = if (photoUri != null) {
                    try {
                        contentResolver.openInputStream(photoUri.toUri())?.use { inputStream ->
                            BitmapFactory.decodeStream(inputStream)
                        }
                    } catch (e: Exception) {
                        null
                    }
                } else null
                val thumbnailBitmap = if (thumbnailUri != null) {
                    try {
                        contentResolver.openInputStream(thumbnailUri.toUri())?.use { inputStream ->
                        BitmapFactory.decodeStream(inputStream)
                        }
                    } catch (e: Exception) {
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

            // Batch-Abfragen für alle Kontakte durchführen
            if (contactIds.isNotEmpty()) {
                val structuredNames = loadStructuredNamesBatch(contentResolver, contactIds)
                val phoneNumbers = loadPhoneNumbersBatch(contentResolver, contactIds)
                val emails = loadEmailsBatch(contentResolver, contactIds)
                val addresses = loadAddressesBatch(contentResolver, contactIds)
                val organizations = loadOrganizationsBatch(contentResolver, contactIds)
                val notes = loadNotesBatch(contentResolver, contactIds)
                val websites = loadWebsitesBatch(contentResolver, contactIds)
                val events = loadEventsBatch(contentResolver, contactIds)
                val ims = loadIMsBatch(contentResolver, contactIds)
                val relations = loadRelationsBatch(contentResolver, contactIds)
                val groups = loadGroupsBatch(contentResolver, contactIds)

                // Daten zu den entsprechenden Kontakten zuordnen
                contacts.forEach { contact ->
                    contact.id?.let { id ->
                        // Strukturierte Namen
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

                        // Telefonnummern
                        contact.phoneNumbers = phoneNumbers[id] ?: emptyList()

                        // E-Mails
                        contact.emails = emails[id] ?: emptyList()

                        // Adressen
                        contact.addresses = addresses[id] ?: emptyList()

                        // Organisation
                        organizations[id]?.let { org ->
                            contact.organization = org.organization
                            contact.department = org.department
                            contact.jobTitle = org.jobTitle
                        }

                        // Notizen
                        contact.note = notes[id]

                        // Websites
                        contact.websites = websites[id] ?: emptyList()

                        // Events
                        contact.events = events[id] ?: emptyList()

                        // IMs
                        contact.ims = ims[id] ?: emptyList()

                        // Beziehungen
                        contact.relations = relations[id] ?: emptyList()

                        // Gruppen
                        contact.groups = groups[id] ?: emptyList()

                        // Is Starred
                        contact.isStarred = (groups[id] ?: emptyList())
                            .any { it.name?.contains("Starred", ignoreCase = true) ?: false }
                    }
                }
            }
        }

        // Filter leere Kontakte heraus
        contacts.filter { contact ->
            !contact.displayName.isNullOrBlank() || hasRelevantData(contact)
        }
    }

// Lazy Loading für Bilder
fun loadContactPhotos(context: Context, contact: Contact) {
    val contentResolver = context.contentResolver

    // Foto laden
    contact.photoUri?.let { uriStr ->
        try {
            val photoUri = uriStr.toUri()
            contentResolver.openInputStream(photoUri)?.use { inputStream ->
                contact.photoBitmap = BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: Exception) {
            // Fehler beim Laden des Fotos ignorieren
        }
    }

    // Thumbnail laden
    contact.thumbnailUri?.let { uriStr ->
        try {
            val thumbnailUri = uriStr.toUri()
            contentResolver.openInputStream(thumbnailUri)?.use { inputStream ->
                contact.thumbnailBitmap = BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: Exception) {
            // Fehler beim Laden des Thumbnails ignorieren
        }
    }
}

private fun hasRelevantData(contact: Contact): Boolean {
    return contact.phoneNumbers.isNotEmpty() ||
            contact.emails.isNotEmpty() ||
            contact.addresses.isNotEmpty() ||
            contact.organization != null ||
            contact.note != null
}

// Hilfsklasse für strukturierte Namen
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

// Hilfsklasse für Organisationsdaten
private data class OrganizationData(
    val organization: String? = null,
    val department: String? = null,
    val jobTitle: String? = null
)

// Batch-Abfrage für strukturierte Namen
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

// Batch-Abfrage für Telefonnummern
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

    cursor?.use {
        val contactIdIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
        val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
        val typeIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)
        val labelIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL)

        while (it.moveToNext()) {
            val contactId = it.getString(contactIdIndex)
            val number = if (numberIndex != -1) it.getString(numberIndex) else null
            val type = if (typeIndex != -1) it.getInt(typeIndex) else 0
            val label = if (labelIndex != -1) it.getString(labelIndex) else null

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

            if (!result.containsKey(contactId)) {
                result[contactId] = mutableListOf()
            }

            result[contactId]?.add(PhoneNumber(number ?: "", typeStr, label))
        }
    }

    return result
}

// Weitere Batch-Abfrage-Methoden...
// (Implementierung der anderen Methoden ähnlich wie oben)

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
            // YYYY-MM-DD oder YYYYMMDD oder --MM-DD oder MMDD
            var year: Int? = null
            var month: Int? = null
            var day: Int? = null
            if (startDate != null) {
                val date = startDate.replace("-", "")
                if (date.length == 8) {
                    year = date.substring(0, 4).toIntOrNull()
                    month = date.substring(4, 6).toIntOrNull()
                    day = date.substring(6, 8).toIntOrNull()
                } else if (date.length == 4) {
                    month = date.substring(0, 2).toIntOrNull()
                    day = date.substring(2, 4).toIntOrNull()
                } else {
                    throw IllegalArgumentException("Invalid date format: $startDate")
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

private fun loadIMsBatch(contentResolver: ContentResolver, contactIds: List<String>): Map<String, List<IM>> {
    val result = mutableMapOf<String, MutableList<IM>>()
    if (contactIds.isEmpty()) return result
    val selection = "${ContactsContract.Data.CONTACT_ID} IN (${contactIds.joinToString(",")}) AND ${ContactsContract.Data.MIMETYPE} = ?"
    val selectionArgs = arrayOf(ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
    val cursor = contentResolver.query(
        ContactsContract.Data.CONTENT_URI,
        null,
        selection,
        selectionArgs,
        null
    )
    cursor?.use {
        val contactIdIndex = it.getColumnIndex(ContactsContract.Data.CONTACT_ID)
        val dataIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA)
        val protocolIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Im.PROTOCOL)
        val typeIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Im.TYPE)
        val labelIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Im.LABEL)
        while (it.moveToNext()) {
            val contactId = it.getString(contactIdIndex)
            val data = if (dataIndex != -1) it.getString(dataIndex) else null
            val protocol = if (protocolIndex != -1) it.getInt(protocolIndex) else 0
            val type = if (typeIndex != -1) it.getInt(typeIndex) else 0
            val label = if (labelIndex != -1) it.getString(labelIndex) else null
            val protocolStr = when (protocol) {
                ContactsContract.CommonDataKinds.Im.PROTOCOL_CUSTOM -> "custom"
                ContactsContract.CommonDataKinds.Im.PROTOCOL_AIM -> "aim"
                ContactsContract.CommonDataKinds.Im.PROTOCOL_MSN -> "msn"
                ContactsContract.CommonDataKinds.Im.PROTOCOL_YAHOO -> "yahoo"
                ContactsContract.CommonDataKinds.Im.PROTOCOL_SKYPE -> "skype"
                ContactsContract.CommonDataKinds.Im.PROTOCOL_QQ -> "qq"
                ContactsContract.CommonDataKinds.Im.PROTOCOL_GOOGLE_TALK -> "gtalk"
                ContactsContract.CommonDataKinds.Im.PROTOCOL_ICQ -> "icq"
                ContactsContract.CommonDataKinds.Im.PROTOCOL_JABBER -> "jabber"
                ContactsContract.CommonDataKinds.Im.PROTOCOL_NETMEETING -> "netmeeting"
                else -> "other"
            }
            val typeStr = when (type) {
                ContactsContract.CommonDataKinds.Im.TYPE_HOME -> "home"
                ContactsContract.CommonDataKinds.Im.TYPE_WORK -> "work"
                ContactsContract.CommonDataKinds.Im.TYPE_OTHER -> "other"
                ContactsContract.CommonDataKinds.Im.TYPE_CUSTOM -> "custom"
                else -> "unknown"
            }
            if (data != null) {
                val im = IM(
                    protocol = data,
                    handle = protocolStr,
                    type = typeStr,
                    label = label
                )
                if (result[contactId] == null) result[contactId] = mutableListOf()
                result[contactId]?.add(im)
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
                result[contactId]?.add(
                    Group(
                        id = groupId,
                        name = groupNameCache.getOrPut(groupId) {
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
                    )
                )
            }
        }
    }
    return result
}