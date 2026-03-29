package de.benkralex.socius.data.contacts.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import de.benkralex.socius.data.contacts.local.database.localContactsTypeConverter.*
import de.benkralex.socius.data.model.Address
import de.benkralex.socius.data.model.Contact
import de.benkralex.socius.data.model.ContactOrigin
import de.benkralex.socius.data.model.Email
import de.benkralex.socius.data.model.Event
import de.benkralex.socius.data.model.Job
import de.benkralex.socius.data.model.Name
import de.benkralex.socius.data.model.Phone
import de.benkralex.socius.data.model.ProfilePicture
import de.benkralex.socius.data.model.Relation
import de.benkralex.socius.data.model.Website

@Entity(tableName = "LocalContacts")
data class LocalContactsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var origin: ContactOrigin = ContactOrigin.LOCAL,

    @field:TypeConverters(NameTypeConverter::class)
    var name: Name = Name(),
    @field:TypeConverters(JobTypeConverter::class)
    var job: Job = Job(),
    @field:TypeConverters(ProfilePictureTypeConverter::class)
    var profilePicture: ProfilePicture = ProfilePicture(),

    @field:TypeConverters(PhoneTypeConverter::class)
    var phoneNumbers: List<Phone> = emptyList(),
    @field:TypeConverters(EmailTypeConverter::class)
    var emails: List<Email> = emptyList(),
    @field:TypeConverters(AddressTypeConverter::class)
    var addresses: List<Address> = emptyList(),
    @field:TypeConverters(WebsiteTypeConverter::class)
    var websites: List<Website> = emptyList(),
    @field:TypeConverters(RelationTypeConverter::class)
    var relations: List<Relation> = emptyList(),
    @field:TypeConverters(EventTypeConverter::class)
    var events: List<Event> = emptyList(),
    @field:TypeConverters(StringListTypeConverter::class)
    var groups: List<String> = emptyList(),

    var note: String? = null,
    var isStarred: Boolean = false,
    @field:TypeConverters(StringMapTypeConverter::class)
    var customFields: Map<String, String> = emptyMap()
) {
    fun toContact(): Contact {
        return Contact(
            id = id,
            origin = origin,
            name = name,
            job = job,
            profilePicture = profilePicture,
            phoneNumbers = phoneNumbers,
            emails = emails,
            addresses = addresses,
            websites = websites,
            relations = relations,
            events = events,
            groups = groups,
            note = note,
            isStarred = isStarred,
            customFields = customFields
        )
    }

    companion object {
        fun fromContact(contact: Contact): LocalContactsEntity {
            return if (contact.id == null) {
                LocalContactsEntity(
                    origin = contact.origin,
                    name = contact.name,
                    job = contact.job,
                    profilePicture = contact.profilePicture,
                    phoneNumbers = contact.phoneNumbers,
                    emails = contact.emails,
                    addresses = contact.addresses,
                    websites = contact.websites,
                    relations = contact.relations,
                    events = contact.events,
                    groups = contact.groups,
                    note = contact.note,
                    isStarred = contact.isStarred,
                    customFields = contact.customFields
                )
            } else {
                LocalContactsEntity(
                    id = contact.id!!,
                    origin = contact.origin,
                    name = contact.name,
                    job = contact.job,
                    profilePicture = contact.profilePicture,
                    phoneNumbers = contact.phoneNumbers,
                    emails = contact.emails,
                    addresses = contact.addresses,
                    websites = contact.websites,
                    relations = contact.relations,
                    events = contact.events,
                    groups = contact.groups,
                    note = contact.note,
                    isStarred = contact.isStarred,
                    customFields = contact.customFields
                )
            }
        }
    }
}
