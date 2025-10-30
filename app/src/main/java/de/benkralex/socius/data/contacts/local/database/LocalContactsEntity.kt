package de.benkralex.socius.data.contacts.local.database

import android.content.Context
import android.graphics.BitmapFactory
import androidx.core.net.toUri
import androidx.room.Entity
import androidx.room.PrimaryKey
import de.benkralex.socius.data.Contact
import de.benkralex.socius.data.ContactEvent
import de.benkralex.socius.data.ContactOrigin
import de.benkralex.socius.data.Email
import de.benkralex.socius.data.Group
import de.benkralex.socius.data.PhoneNumber
import de.benkralex.socius.data.PostalAddress
import de.benkralex.socius.data.Relation
import de.benkralex.socius.data.Website

@Entity(tableName = "local_contacts")
data class LocalContactsEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    var prefix: String? = null,
    var givenName: String? = null,
    var middleName: String? = null,
    var familyName: String? = null,
    var suffix: String? = null,
    var nickname: String? = null,
    var phoneticGivenName: String? = null,
    var phoneticMiddleName: String? = null,
    var phoneticFamilyName: String? = null,

    var organization: String? = null,
    var department: String? = null,
    var jobTitle: String? = null,

    var note: String? = null,
    var isStarred: Boolean = false,

    var photoUri: String? = null,
    var thumbnailUri: String? = null,

    var phoneNumbers: List<PhoneNumber> = emptyList(),
    var emails: List<Email> = emptyList(),
    var addresses: List<PostalAddress> = emptyList(),
    var websites: List<Website> = emptyList(),
    var relations: List<Relation> = emptyList(),
    var events: List<ContactEvent> = emptyList(),
    var groups: List<Group> = emptyList(),

    var customFields: Map<String, String> = emptyMap()
) {
    companion object {
        fun fromContact(contact: Contact): LocalContactsEntity {
            return LocalContactsEntity(
                prefix = contact.prefix,
                givenName = contact.givenName,
                middleName = contact.middleName,
                familyName = contact.familyName,
                suffix = contact.suffix,
                nickname = contact.nickname,
                phoneticGivenName = contact.phoneticGivenName,
                phoneticMiddleName = contact.phoneticMiddleName,
                phoneticFamilyName = contact.phoneticFamilyName,
                organization = contact.organization,
                department = contact.department,
                jobTitle = contact.jobTitle,
                note = contact.note,
                isStarred = contact.isStarred,
                photoUri = contact.photoUri,
                thumbnailUri = contact.thumbnailUri,
                phoneNumbers = contact.phoneNumbers,
                emails = contact.emails,
                addresses = contact.addresses,
                websites = contact.websites,
                relations = contact.relations,
                events = contact.events,
                groups = contact.groups,
                customFields = contact.customFields
            )
        }
    }

    fun toContact(context: Context): Contact {
        val contentResolver = context.contentResolver
        val photoBitmap = if (photoUri != null) {
            try {
                contentResolver.openInputStream(photoUri!!.toUri())?.use { inputStream ->
                    BitmapFactory.decodeStream(inputStream)
                }
            } catch (_: Exception) {
                null
            }
        } else null

        val thumbnailBitmap = if (thumbnailUri != null) {
            try {
                contentResolver.openInputStream(thumbnailUri!!.toUri())?.use { inputStream ->
                    BitmapFactory.decodeStream(inputStream)
                }
            } catch (_: Exception) {
                null
            }
        } else null

        return Contact(
            id = this.id.toString(),
            origin = ContactOrigin.LOCAL,
            prefix = prefix,
            givenName = givenName,
            middleName = middleName,
            familyName = familyName,
            suffix = suffix,
            nickname = nickname,
            phoneticGivenName = phoneticGivenName,
            phoneticMiddleName = phoneticMiddleName,
            phoneticFamilyName = phoneticFamilyName,
            organization = organization,
            department = department,
            jobTitle = jobTitle,
            note = note,
            isStarred = isStarred,
            photoUri = photoUri,
            photoBitmap = photoBitmap,
            thumbnailUri = thumbnailUri,
            thumbnailBitmap = thumbnailBitmap,
            phoneNumbers = phoneNumbers,
            emails = emails,
            addresses = addresses,
            websites = websites,
            relations = relations,
            events = events,
            groups = groups,
            customFields = customFields,

            displayName = null,
            birthday = null,
            anniversary = null,
        )
    }
}