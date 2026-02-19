package de.benkralex.socius.data.import_export

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import de.benkralex.socius.data.Contact
import de.benkralex.socius.data.ContactEvent
import de.benkralex.socius.data.ContactOrigin
import de.benkralex.socius.data.Email
import de.benkralex.socius.data.PhoneNumber
import de.benkralex.socius.data.PostalAddress
import de.benkralex.socius.data.Relation
import de.benkralex.socius.data.Website
import ezvcard.VCard
import ezvcard.android.AndroidCustomFieldScribe
import ezvcard.io.text.VCardReader
import ezvcard.parameter.AddressType
import ezvcard.parameter.EmailType
import ezvcard.parameter.RelatedType
import ezvcard.parameter.TelephoneType
import ezvcard.property.DateOrTimeProperty
import ezvcard.util.IOUtils.closeQuietly
import java.util.UUID

fun vCardToContacts(file: List<String>): List<Contact> {
    val contacts: MutableList<Contact> = mutableListOf()
    var reader: VCardReader? = null
    try {
        reader = VCardReader(file.joinToString("\n"))
        reader.registerScribe(AndroidCustomFieldScribe())

        var vcard: VCard? = reader.readNext()
        while (vcard != null) {
            contacts.add(vcard.toContact())
            vcard = reader.readNext()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        closeQuietly(reader)
    }
    return contacts
}

fun VCard.toContact(): Contact {
    val sn = structuredName
    val org = organization

    val photoBitmap = photos.firstOrNull()?.let { photo ->
        decodeToBitmap(photo.data) ?: photo.url?.let { url ->
            try {
                val decoded = Base64.decode(url, Base64.DEFAULT)
                decodeToBitmap(decoded)
            } catch (_: Exception) {
                null
            }
        }
    }

    return Contact(
        id = uid?.value ?: UUID.randomUUID().toString(),
        origin = ContactOrigin.IMPORT,
        displayName = formattedName?.value,
        prefix = sn?.prefixes?.joinToString(" ")?.takeIf { it.isNotBlank() },
        givenName = sn?.given?.takeIf { it.isNotBlank() },
        middleName = sn?.additionalNames?.joinToString(" ")?.takeIf { it.isNotBlank() },
        familyName = sn?.family?.takeIf { it.isNotBlank() },
        suffix = sn?.suffixes?.joinToString(" ")?.takeIf { it.isNotBlank() },
        nickname = nickname?.values?.firstOrNull(),
        organization = org?.values?.firstOrNull()?.takeIf { it.isNotBlank() },
        department = org?.values?.drop(1)?.joinToString(", ")?.takeIf { it.isNotBlank() },
        jobTitle = titles.firstOrNull()?.value?.takeIf { it.isNotBlank() },
        note = notes.firstOrNull()?.value?.takeIf { it.isNotBlank() },
        birthday = birthday?.toDateString(),
        anniversary = anniversary?.toDateString(),
        photoBitmap = photoBitmap,
        phoneNumbers = telephoneNumbers.map { tel ->
            PhoneNumber(
                number = tel.text ?: "",
                type = tel.types.firstOrNull()?.toPhoneType() ?: "other",
                label = tel.types.firstOrNull()?.value
            )
        },
        emails = emails.map { email ->
            Email(
                address = email.value ?: "",
                type = email.types.firstOrNull()?.toEmailType() ?: "other",
                label = email.types.firstOrNull()?.value
            )
        },
        addresses = addresses.map { addr ->
            PostalAddress(
                street = addr.streetAddress?.takeIf { it.isNotBlank() },
                city = addr.locality?.takeIf { it.isNotBlank() },
                region = addr.region?.takeIf { it.isNotBlank() },
                postcode = addr.postalCode?.takeIf { it.isNotBlank() },
                country = addr.country?.takeIf { it.isNotBlank() },
                type = addr.types.firstOrNull()?.toAddressType() ?: "other",
                label = addr.types.firstOrNull()?.value
            )
        },
        websites = urls.map { url ->
            Website(
                url = url.value ?: "",
                type = url.type ?: "other",
                label = url.type
            )
        },
        relations = relations.map { rel ->
            Relation(
                name = rel.text ?: rel.uri ?: "",
                type = rel.types.firstOrNull()?.toRelationType() ?: "other",
                label = rel.types.firstOrNull()?.value
            )
        },
        events = buildList {
            birthday?.let { bday ->
                bday.date?.let { date ->
                    val cal = java.util.Calendar.getInstance().apply { time = date }
                    add(
                        ContactEvent(
                            day = cal.get(java.util.Calendar.DAY_OF_MONTH),
                            month = cal.get(java.util.Calendar.MONTH) + 1,
                            year = cal.get(java.util.Calendar.YEAR),
                            type = "birthday"
                        )
                    )
                } ?: bday.partialDate?.let { pd ->
                    add(
                        ContactEvent(
                            day = pd.date,
                            month = pd.month,
                            year = pd.year,
                            type = "birthday"
                        )
                    )
                }
            }
            anniversary?.let { anniv ->
                anniv.date?.let { date ->
                    val cal = java.util.Calendar.getInstance().apply { time = date }
                    add(
                        ContactEvent(
                            day = cal.get(java.util.Calendar.DAY_OF_MONTH),
                            month = cal.get(java.util.Calendar.MONTH) + 1,
                            year = cal.get(java.util.Calendar.YEAR),
                            type = "anniversary"
                        )
                    )
                } ?: anniv.partialDate?.let { pd ->
                    add(
                        ContactEvent(
                            day = pd.date,
                            month = pd.month,
                            year = pd.year,
                            type = "anniversary"
                        )
                    )
                }
            }
        }
    )
}

private fun DateOrTimeProperty.toDateString(): String? {
    date?.let { date ->
        val cal = java.util.Calendar.getInstance().apply { time = date }
        val year = cal.get(java.util.Calendar.YEAR)
        val month = cal.get(java.util.Calendar.MONTH) + 1
        val day = cal.get(java.util.Calendar.DAY_OF_MONTH)
        return "%04d-%02d-%02d".format(year, month, day)
    }
    partialDate?.let { pd ->
        val year = pd.year
        val month = pd.month
        val day = pd.date
        if (year != null && month != null && day != null) {
            return "%04d-%02d-%02d".format(year, month, day)
        }
        if (month != null && day != null) {
            return "----%02d-%02d".format(month, day)
        }
    }
    return null
}

private fun TelephoneType.toPhoneType(): String {
    return when (this) {
        TelephoneType.CELL -> "mobile"
        TelephoneType.HOME -> "home"
        TelephoneType.WORK -> "work"
        TelephoneType.FAX -> "fax_work"
        TelephoneType.PAGER -> "pager"
        TelephoneType.TEXT -> "other"
        TelephoneType.VOICE -> "other"
        else -> "other"
    }
}

private fun EmailType.toEmailType(): String {
    return when (this) {
        EmailType.HOME -> "home"
        EmailType.WORK -> "work"
        else -> "other"
    }
}

private fun AddressType.toAddressType(): String {
    return when (this) {
        AddressType.HOME -> "home"
        AddressType.WORK -> "work"
        else -> "other"
    }
}

private fun RelatedType.toRelationType(): String {
    return when (this) {
        RelatedType.SPOUSE -> "spouse"
        RelatedType.CHILD -> "child"
        RelatedType.PARENT -> "parent"
        RelatedType.SIBLING -> "sibling"
        RelatedType.FRIEND -> "friend"
        else -> "other"
    }
}

private fun decodeToBitmap(data: ByteArray?): Bitmap? {
    if (data == null || data.isEmpty()) return null
    return try {
        BitmapFactory.decodeByteArray(data, 0, data.size)
    } catch (_: Exception) {
        null
    }
}