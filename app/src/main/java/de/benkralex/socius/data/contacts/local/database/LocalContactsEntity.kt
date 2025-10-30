package de.benkralex.socius.data.contacts.local.database

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
data class LocalContactsEntity(
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
    fun toContact(): Contact {
        return Contact(
            id = this.id.toString(),
            origin = ContactOrigin.LOCAL,
            prefix = this.prefix,
            givenName = this.givenName,
            middleName = this.middleName,
            familyName = this.familyName,
            suffix = this.suffix,
            nickname = this.nickname,
            phoneticGivenName = this.phoneticGivenName,
            phoneticMiddleName = this.phoneticMiddleName,
            phoneticFamilyName = this.phoneticFamilyName,
            organization = this.organization,
            department = this.department,
            jobTitle = this.jobTitle,
            note = this.note,
            isStarred = this.isStarred,
            photoUri = this.photoUri,
            thumbnailUri = this.thumbnailUri,
            phoneNumbers = this.phoneNumbers,
            emails = this.emails,
            addresses = this.addresses,
            websites = this.websites,
            relations = this.relations,
            events = this.events,
            groups = this.groups,
            customFields = this.customFields,

            displayName = null,
            birthday = null,
            anniversary = null,
            photoBitmap = null,
            thumbnailBitmap = null,
        )
    }
}