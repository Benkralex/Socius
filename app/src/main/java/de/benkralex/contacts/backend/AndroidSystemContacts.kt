package de.benkralex.contacts.backend

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.ContactsContract

fun getAndroidSystemContacts(context: Context): List<Contact> {
    val contacts = mutableListOf<Contact>()
    val contentResolver = context.contentResolver

    val cursor = contentResolver.query(
        ContactsContract.Contacts.CONTENT_URI,
        null,
        null,
        null,
        "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} ASC"
    )

    cursor?.use {
        val idIndex = it.getColumnIndex(ContactsContract.Contacts._ID)
        val displayNameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
        val photoUriIndex = it.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)
        val thumbnailUriIndex = it.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)

        while (it.moveToNext()) {
            val contactId = it.getString(idIndex)
            val displayName = it.getString(displayNameIndex)

            val contact = Contact(
                id = contactId,
                displayName = displayName
            )

            // Photo URI und Bitmap
            if (photoUriIndex != -1) {
                val photoUriStr = it.getString(photoUriIndex)
                contact.photoUri = photoUriStr
                photoUriStr?.let { uriStr ->
                    try {
                        val photoUri = Uri.parse(uriStr)
                        contentResolver.openInputStream(photoUri)?.use { inputStream ->
                            contact.photoBitmap = BitmapFactory.decodeStream(inputStream)
                        }
                    } catch (e: Exception) {
                        // Fehler beim Laden des Fotos ignorieren
                    }
                }
            }

            // Thumbnail URI und Bitmap
            if (thumbnailUriIndex != -1) {
                val thumbnailUriStr = it.getString(thumbnailUriIndex)
                contact.thumbnailUri = thumbnailUriStr
                thumbnailUriStr?.let { uriStr ->
                    try {
                        val thumbnailUri = Uri.parse(uriStr)
                        contentResolver.openInputStream(thumbnailUri)?.use { inputStream ->
                            contact.thumbnailBitmap = BitmapFactory.decodeStream(inputStream)
                        }
                    } catch (e: Exception) {
                        // Fehler beim Laden des Thumbnails ignorieren
                    }
                }
            }

            // Strukturierte Namen laden
            loadStructuredName(contentResolver, contactId, contact)

            // Telefonnummern laden
            contact.phoneNumbers = loadPhoneNumbers(contentResolver, contactId)

            // E-Mail-Adressen laden
            contact.emails = loadEmails(contentResolver, contactId)

            // Adressen laden
            contact.addresses = loadAddresses(contentResolver, contactId)

            // Organisation laden
            loadOrganization(contentResolver, contactId, contact)

            // Notizen laden
            loadNote(contentResolver, contactId, contact)

            // Websites laden
            contact.websites = loadWebsites(contentResolver, contactId)

            // Events (Geburtstag, etc.) laden
            contact.events = loadEvents(contentResolver, contactId)

            // IM-Adressen laden
            contact.ims = loadIMs(contentResolver, contactId)

            // Beziehungen laden
            contact.relations = loadRelations(contentResolver, contactId)

            if (!displayName.isNullOrBlank() || hasRelevantData(contact)) {
                contacts.add(contact)
            }
        }
    }

    return contacts
}

private fun hasRelevantData(contact: Contact): Boolean {
    return contact.phoneNumbers.isNotEmpty() ||
            contact.emails.isNotEmpty() ||
            contact.addresses.isNotEmpty() ||
            contact.organization != null ||
            contact.note != null
}

private fun loadStructuredName(contentResolver: android.content.ContentResolver, contactId: String, contact: Contact) {
    val cursor = contentResolver.query(
        ContactsContract.Data.CONTENT_URI,
        null,
        "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
        arrayOf(contactId, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE),
        null
    )

    cursor?.use {
        if (it.moveToFirst()) {
            val prefixIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PREFIX)
            if (prefixIndex != -1) {
                contact.prefix = it.getString(prefixIndex)
            }

            val givenNameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME)
            if (givenNameIndex != -1) {
                contact.givenName = it.getString(givenNameIndex)
            }

            val middleNameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME)
            if (middleNameIndex != -1) {
                contact.middleName = it.getString(middleNameIndex)
            }

            val familyNameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME)
            if (familyNameIndex != -1) {
                contact.familyName = it.getString(familyNameIndex)
            }

            val suffixIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.SUFFIX)
            if (suffixIndex != -1) {
                contact.suffix = it.getString(suffixIndex)
            }

            val phoneticGivenNameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_GIVEN_NAME)
            if (phoneticGivenNameIndex != -1) {
                contact.phoneticGivenName = it.getString(phoneticGivenNameIndex)
            }

            val phoneticMiddleNameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_MIDDLE_NAME)
            if (phoneticMiddleNameIndex != -1) {
                contact.phoneticMiddleName = it.getString(phoneticMiddleNameIndex)
            }

            val phoneticFamilyNameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_FAMILY_NAME)
            if (phoneticFamilyNameIndex != -1) {
                contact.phoneticFamilyName = it.getString(phoneticFamilyNameIndex)
            }
        }
    }
}

private fun loadPhoneNumbers(contentResolver: android.content.ContentResolver, contactId: String): List<PhoneNumber> {
    val phoneNumbers = mutableListOf<PhoneNumber>()

    val cursor = contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null,
        "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
        arrayOf(contactId),
        null
    )

    cursor?.use {
        while (it.moveToNext()) {
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val typeIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)
            val labelIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL)

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
                else -> "unknown"
            }

            phoneNumbers.add(PhoneNumber(number ?: "", typeStr, label))
        }
    }

    return phoneNumbers
}

private fun loadEmails(contentResolver: android.content.ContentResolver, contactId: String): List<Email> {
    val emails = mutableListOf<Email>()

    val cursor = contentResolver.query(
        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
        null,
        "${ContactsContract.CommonDataKinds.Email.CONTACT_ID} = ?",
        arrayOf(contactId),
        null
    )

    cursor?.use {
        while (it.moveToNext()) {
            val addressIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
            val typeIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE)
            val labelIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Email.LABEL)

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

            emails.add(Email(address ?: "", typeStr, label))
        }
    }

    return emails
}

private fun loadAddresses(contentResolver: android.content.ContentResolver, contactId: String): List<PostalAddress> {
    val addresses = mutableListOf<PostalAddress>()

    val cursor = contentResolver.query(
        ContactsContract.Data.CONTENT_URI,
        null,
        "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
        arrayOf(contactId, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE),
        null
    )

    cursor?.use {
        while (it.moveToNext()) {
            val streetIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET)
            val street = if (streetIndex != -1) it.getString(streetIndex) else null

            val cityIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY)
            val city = if (cityIndex != -1) it.getString(cityIndex) else null

            val regionIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION)
            val region = if (regionIndex != -1) it.getString(regionIndex) else null

            val postcodeIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE)
            val postcode = if (postcodeIndex != -1) it.getString(postcodeIndex) else null

            val countryIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY)
            val country = if (countryIndex != -1) it.getString(countryIndex) else null

            val typeIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE)
            val type = if (typeIndex != -1) it.getInt(typeIndex) else 0

            val labelIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.LABEL)
            val label = if (labelIndex != -1) it.getString(labelIndex) else null

            val typeStr = when (type) {
                ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME -> "home"
                ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK -> "work"
                ContactsContract.CommonDataKinds.StructuredPostal.TYPE_OTHER -> "other"
                ContactsContract.CommonDataKinds.StructuredPostal.TYPE_CUSTOM -> "custom"
                else -> "unknown"
            }

            addresses.add(PostalAddress(street, city, region, postcode, country, typeStr, label))
        }
    }

    return addresses
}

private fun loadOrganization(contentResolver: android.content.ContentResolver, contactId: String, contact: Contact) {
    val cursor = contentResolver.query(
        ContactsContract.Data.CONTENT_URI,
        null,
        "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
        arrayOf(contactId, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE),
        null
    )

    cursor?.use {
        if (it.moveToFirst()) {
            val companyIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY)
            if (companyIndex != -1) {
                contact.organization = it.getString(companyIndex)
            }

            val departmentIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DEPARTMENT)
            if (departmentIndex != -1) {
                contact.department = it.getString(departmentIndex)
            }

            val titleIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE)
            if (titleIndex != -1) {
                contact.jobTitle = it.getString(titleIndex)
            }
        }
    }
}

private fun loadNote(contentResolver: android.content.ContentResolver, contactId: String, contact: Contact) {
    val cursor = contentResolver.query(
        ContactsContract.Data.CONTENT_URI,
        null,
        "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
        arrayOf(contactId, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE),
        null
    )

    cursor?.use {
        if (it.moveToFirst()) {
            val noteIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE)
            if (noteIndex != -1) {
                contact.note = it.getString(noteIndex)
            }
        }
    }
}

private fun loadWebsites(contentResolver: android.content.ContentResolver, contactId: String): List<Website> {
    val websites = mutableListOf<Website>()

    val cursor = contentResolver.query(
        ContactsContract.Data.CONTENT_URI,
        null,
        "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
        arrayOf(contactId, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE),
        null
    )

    cursor?.use {
        while (it.moveToNext()) {
            val urlIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Website.URL)
            val url = if (urlIndex != -1) it.getString(urlIndex) else null

            val typeIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Website.TYPE)
            val type = if (typeIndex != -1) it.getInt(typeIndex) else 0

            val labelIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Website.LABEL)
            val label = if (labelIndex != -1) it.getString(labelIndex) else null

            val typeStr = when (type) {
                ContactsContract.CommonDataKinds.Website.TYPE_HOMEPAGE -> "homepage"
                ContactsContract.CommonDataKinds.Website.TYPE_BLOG -> "blog"
                ContactsContract.CommonDataKinds.Website.TYPE_PROFILE -> "profile"
                ContactsContract.CommonDataKinds.Website.TYPE_HOME -> "home"
                ContactsContract.CommonDataKinds.Website.TYPE_WORK -> "work"
                ContactsContract.CommonDataKinds.Website.TYPE_FTP -> "ftp"
                ContactsContract.CommonDataKinds.Website.TYPE_OTHER -> "other"
                ContactsContract.CommonDataKinds.Website.TYPE_CUSTOM -> "custom"
                else -> "unknown"
            }

            websites.add(Website(url ?: "", typeStr, label))
        }
    }

    return websites
}

private fun loadEvents(contentResolver: android.content.ContentResolver, contactId: String): List<ContactEvent> {
    val events = mutableListOf<ContactEvent>()

    val cursor = contentResolver.query(
        ContactsContract.Data.CONTENT_URI,
        null,
        "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
        arrayOf(contactId, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE),
        null
    )

    cursor?.use {
        while (it.moveToNext()) {
            val dateIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE)
            val date = if (dateIndex != -1) it.getString(dateIndex) else null

            val typeIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Event.TYPE)
            val type = if (typeIndex != -1) it.getInt(typeIndex) else 0

            val labelIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Event.LABEL)
            val label = if (labelIndex != -1) it.getString(labelIndex) else null

            val typeStr = when (type) {
                ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY -> "birthday"
                ContactsContract.CommonDataKinds.Event.TYPE_ANNIVERSARY -> "anniversary"
                ContactsContract.CommonDataKinds.Event.TYPE_OTHER -> "other"
                ContactsContract.CommonDataKinds.Event.TYPE_CUSTOM -> "custom"
                else -> "unknown"
            }

            events.add(ContactEvent(date ?: "", typeStr, label))
        }
    }

    return events
}

private fun loadIMs(contentResolver: android.content.ContentResolver, contactId: String): List<IM> {
    val ims = mutableListOf<IM>()

    val cursor = contentResolver.query(
        ContactsContract.Data.CONTENT_URI,
        null,
        "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
        arrayOf(contactId, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE),
        null
    )

    cursor?.use {
        while (it.moveToNext()) {
            val handleIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA)
            val handle = if (handleIndex != -1) it.getString(handleIndex) else null

            val protocolIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Im.PROTOCOL)
            val protocol = if (protocolIndex != -1) it.getInt(protocolIndex) else 0

            val typeIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Im.TYPE)
            val type = if (typeIndex != -1) it.getInt(typeIndex) else 0

            val labelIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Im.LABEL)
            val label = if (labelIndex != -1) it.getString(labelIndex) else null

            val protocolStr = when (protocol) {
                ContactsContract.CommonDataKinds.Im.PROTOCOL_AIM -> "AIM"
                ContactsContract.CommonDataKinds.Im.PROTOCOL_MSN -> "MSN"
                ContactsContract.CommonDataKinds.Im.PROTOCOL_YAHOO -> "Yahoo"
                ContactsContract.CommonDataKinds.Im.PROTOCOL_SKYPE -> "Skype"
                ContactsContract.CommonDataKinds.Im.PROTOCOL_QQ -> "QQ"
                ContactsContract.CommonDataKinds.Im.PROTOCOL_GOOGLE_TALK -> "Google Talk"
                ContactsContract.CommonDataKinds.Im.PROTOCOL_ICQ -> "ICQ"
                ContactsContract.CommonDataKinds.Im.PROTOCOL_JABBER -> "Jabber"
                ContactsContract.CommonDataKinds.Im.PROTOCOL_NETMEETING -> "NetMeeting"
                else -> "unknown"
            }

            val typeStr = when (type) {
                ContactsContract.CommonDataKinds.Im.TYPE_HOME -> "home"
                ContactsContract.CommonDataKinds.Im.TYPE_WORK -> "work"
                ContactsContract.CommonDataKinds.Im.TYPE_OTHER -> "other"
                ContactsContract.CommonDataKinds.Im.TYPE_CUSTOM -> "custom"
                else -> "unknown"
            }

            ims.add(IM(protocolStr, handle ?: "", typeStr, label))
        }
    }

    return ims
}

private fun loadRelations(contentResolver: android.content.ContentResolver, contactId: String): List<Relation> {
    val relations = mutableListOf<Relation>()

    val cursor = contentResolver.query(
        ContactsContract.Data.CONTENT_URI,
        null,
        "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
        arrayOf(contactId, ContactsContract.CommonDataKinds.Relation.CONTENT_ITEM_TYPE),
        null
    )

    cursor?.use {
        while (it.moveToNext()) {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Relation.NAME)
            val name = if (nameIndex != -1) it.getString(nameIndex) else null

            val typeIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Relation.TYPE)
            val type = if (typeIndex != -1) it.getInt(typeIndex) else 0

            val labelIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Relation.LABEL)
            val label = if (labelIndex != -1) it.getString(labelIndex) else null

            val typeStr = when (type) {
                ContactsContract.CommonDataKinds.Relation.TYPE_SPOUSE -> "spouse"
                ContactsContract.CommonDataKinds.Relation.TYPE_CHILD -> "child"
                ContactsContract.CommonDataKinds.Relation.TYPE_MOTHER -> "mother"
                ContactsContract.CommonDataKinds.Relation.TYPE_FATHER -> "father"
                ContactsContract.CommonDataKinds.Relation.TYPE_PARENT -> "parent"
                ContactsContract.CommonDataKinds.Relation.TYPE_BROTHER -> "brother"
                ContactsContract.CommonDataKinds.Relation.TYPE_SISTER -> "sister"
                ContactsContract.CommonDataKinds.Relation.TYPE_FRIEND -> "friend"
                ContactsContract.CommonDataKinds.Relation.TYPE_RELATIVE -> "relative"
                ContactsContract.CommonDataKinds.Relation.TYPE_DOMESTIC_PARTNER -> "domestic_partner"
                ContactsContract.CommonDataKinds.Relation.TYPE_MANAGER -> "manager"
                ContactsContract.CommonDataKinds.Relation.TYPE_ASSISTANT -> "assistant"
                ContactsContract.CommonDataKinds.Relation.TYPE_REFERRED_BY -> "referred_by"
                ContactsContract.CommonDataKinds.Relation.TYPE_PARTNER -> "partner"
                ContactsContract.CommonDataKinds.Relation.TYPE_CUSTOM -> "custom"
                else -> "unknown"
            }

            relations.add(Relation(name ?: "", typeStr, label))
        }
    }

    return relations
}