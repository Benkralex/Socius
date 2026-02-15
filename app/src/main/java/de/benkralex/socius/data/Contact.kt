package de.benkralex.socius.data

import android.graphics.Bitmap
import kotlinx.serialization.Serializable

data class Contact(
    var id: String,
    var origin: ContactOrigin,

    var displayName: String? = null,
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

    var birthday: String? = null, // Format: YYYY-MM-DD
    var anniversary: String? = null, // Format: YYYY-MM-DD

    var photoUri: String? = null,
    var photoBitmap: Bitmap? = null,
    var thumbnailUri: String? = null,
    var thumbnailBitmap: Bitmap? = null,

    var phoneNumbers: List<PhoneNumber> = emptyList(),
    var emails: List<Email> = emptyList(),
    var addresses: List<PostalAddress> = emptyList(),
    var websites: List<Website> = emptyList(),
    var relations: List<Relation> = emptyList(),
    var events: List<ContactEvent> = emptyList(),
    var groups: List<Group> = emptyList(),

    var customFields: Map<String, String> = emptyMap()
) {
    fun isReadOnly(): Boolean {
        return origin == ContactOrigin.SYSTEM
                || origin == ContactOrigin.URI
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Contact) return false
        return id == other.id &&
            origin == other.origin &&
                displayName == other.displayName &&
                prefix == other.prefix &&
                givenName == other.givenName &&
                middleName == other.middleName &&
                familyName == other.familyName &&
                suffix == other.suffix &&
                nickname == other.nickname &&
                phoneticGivenName == other.phoneticGivenName &&
                phoneticMiddleName == other.phoneticMiddleName &&
                phoneticFamilyName == other.phoneticFamilyName &&
                organization == other.organization &&
                department == other.department &&
                jobTitle == other.jobTitle &&
                note == other.note &&
                isStarred == other.isStarred &&
                birthday == other.birthday &&
                anniversary == other.anniversary &&
                photoUri == other.photoUri &&
                photoBitmap == other.photoBitmap &&
                thumbnailUri == other.thumbnailUri &&
                thumbnailBitmap == other.thumbnailBitmap &&
                phoneNumbers == other.phoneNumbers &&
                emails == other.emails &&
                addresses == other.addresses &&
                websites == other.websites &&
                relations == other.relations &&
                events == other.events &&
                groups == other.groups &&
                customFields == other.customFields
    }

    override fun hashCode(): Int {
        var result = isStarred.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + origin.hashCode()
        result = 31 * result + (displayName?.hashCode() ?: 0)
        result = 31 * result + (prefix?.hashCode() ?: 0)
        result = 31 * result + (givenName?.hashCode() ?: 0)
        result = 31 * result + (middleName?.hashCode() ?: 0)
        result = 31 * result + (familyName?.hashCode() ?: 0)
        result = 31 * result + (suffix?.hashCode() ?: 0)
        result = 31 * result + (nickname?.hashCode() ?: 0)
        result = 31 * result + (phoneticGivenName?.hashCode() ?: 0)
        result = 31 * result + (phoneticMiddleName?.hashCode() ?: 0)
        result = 31 * result + (phoneticFamilyName?.hashCode() ?: 0)
        result = 31 * result + (organization?.hashCode() ?: 0)
        result = 31 * result + (department?.hashCode() ?: 0)
        result = 31 * result + (jobTitle?.hashCode() ?: 0)
        result = 31 * result + (note?.hashCode() ?: 0)
        result = 31 * result + (birthday?.hashCode() ?: 0)
        result = 31 * result + (anniversary?.hashCode() ?: 0)
        result = 31 * result + (photoUri?.hashCode() ?: 0)
        result = 31 * result + (photoBitmap?.hashCode() ?: 0)
        result = 31 * result + (thumbnailUri?.hashCode() ?: 0)
        result = 31 * result + (thumbnailBitmap?.hashCode() ?: 0)
        result = 31 * result + phoneNumbers.hashCode()
        result = 31 * result + emails.hashCode()
        result = 31 * result + addresses.hashCode()
        result = 31 * result + websites.hashCode()
        result = 31 * result + relations.hashCode()
        result = 31 * result + events.hashCode()
        result = 31 * result + groups.hashCode()
        result = 31 * result + customFields.hashCode()
        return result
    }
}

@Serializable
data class PhoneNumber(
    var number: String,
    var type: String, // mobile, home, work, etc.
    var label: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (other !is PhoneNumber) return false
        return number == other.number &&
                type == other.type &&
                label == other.label
    }

    override fun hashCode(): Int {
        var result = number.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + (label?.hashCode() ?: 0)
        return result
    }
}

@Serializable
data class Email(
    var address: String,
    var type: String,
    var label: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (other !is Email) return false
        return address == other.address &&
                type == other.type &&
                label == other.label
    }

    override fun hashCode(): Int {
        var result = address.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + (label?.hashCode() ?: 0)
        return result
    }
}

@Serializable
data class PostalAddress(
    var street: String? = null,
    var city: String? = null,
    var region: String? = null,
    var postcode: String? = null,
    var country: String? = null,
    var type: String,
    var label: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (other !is PostalAddress) return false
        return street == other.street &&
                city == other.city &&
                region == other.region &&
                postcode == other.postcode &&
                country == other.country &&
                type == other.type &&
                label == other.label
    }

    override fun hashCode(): Int {
        var result = street?.hashCode() ?: 0
        result = 31 * result + (city?.hashCode() ?: 0)
        result = 31 * result + (region?.hashCode() ?: 0)
        result = 31 * result + (postcode?.hashCode() ?: 0)
        result = 31 * result + (country?.hashCode() ?: 0)
        result = 31 * result + type.hashCode()
        result = 31 * result + (label?.hashCode() ?: 0)
        return result
    }
}

@Serializable
data class Website(
    var url: String,
    var type: String,
    var label: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (other !is Website) return false
        return url == other.url &&
                type == other.type &&
                label == other.label
    }

    override fun hashCode(): Int {
        var result = url.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + (label?.hashCode() ?: 0)
        return result
    }
}

@Serializable
data class Relation(
    var name: String,
    var type: String, // spouse, child, parent, etc.
    var label: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (other !is Relation) return false
        return name == other.name &&
                type == other.type &&
                label == other.label
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + (label?.hashCode() ?: 0)
        return result
    }
}

@Serializable
data class ContactEvent(
    var day: Int? = null,
    var month: Int? = null,
    var year: Int? = null,
    var type: String, // birthday, anniversary, etc.
    var label: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (other !is ContactEvent) return false
        return day == other.day &&
                month == other.month &&
                year == other.year &&
                type == other.type &&
                label == other.label
    }

    override fun hashCode(): Int {
        var result = day ?: 0
        result = 31 * result + (month ?: 0)
        result = 31 * result + (year ?: 0)
        result = 31 * result + type.hashCode()
        result = 31 * result + (label?.hashCode() ?: 0)
        return result
    }
}

@Serializable
data class Group(
    var id: Long,
    var name: String?,
) {
    override fun equals(other: Any?): Boolean {
        if (other !is Group) return false
        return id == other.id &&
                name == other.name
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        return result
    }
}

@Serializable
enum class ContactOrigin {
    SYSTEM,
    LOCAL,
    REMOTE,
    URI,
    IMPORT,
}