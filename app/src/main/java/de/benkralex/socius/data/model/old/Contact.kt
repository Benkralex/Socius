package de.benkralex.socius.data.model.old

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.net.toUri
import de.benkralex.socius.data.model.Address
import de.benkralex.socius.data.model.BitmapSerializer
import de.benkralex.socius.data.model.Job
import de.benkralex.socius.data.model.Name
import de.benkralex.socius.data.model.Phone
import de.benkralex.socius.data.model.ProfilePicture
import de.benkralex.socius.data.model.Type
import kotlinx.serialization.Serializable
import kotlin.collections.last
import kotlin.text.isDigit
import kotlin.text.split

@Serializable
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
    @Serializable(with = BitmapSerializer::class)
    var photoBitmap: Bitmap? = null,
    var thumbnailUri: String? = null,
    @Serializable(with = BitmapSerializer::class)
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
        return false
    }

    fun toNewContact(context: Context? = null): de.benkralex.socius.data.model.Contact {
        return de.benkralex.socius.data.model.Contact(
            id = id.toIntOrNull(),
            origin = when (origin) {
                ContactOrigin.LOCAL -> de.benkralex.socius.data.model.ContactOrigin.LOCAL
                ContactOrigin.IMPORT -> de.benkralex.socius.data.model.ContactOrigin.TEMPORARY
            },
            name = Name(
                prefix = prefix,
                firstname = givenName,
                secondName = middleName,
                lastname = familyName,
                suffix = suffix,
                nickname = nickname,
                displayName = displayName,
                phoneticFirstname = phoneticGivenName,
                phoneticSecondName = phoneticMiddleName,
                phoneticLastname = phoneticFamilyName,
            ),
            job = Job(
                organization = organization,
                department = department,
                jobTitle = jobTitle,
            ),
            profilePicture = ProfilePicture(getProfileBitmap(context)),
            phoneNumbers = phoneNumbers.map {
                Phone(
                    value = it.number,
                    type = Type.convertPhoneType(if (it.type == "custom") it.label ?: "" else it.type),
                )
            },
            emails = emails.map {
                de.benkralex.socius.data.model.Email(
                    value = it.address,
                    type = Type.convertEmailType(if (it.type == "custom") it.label ?: "" else it.type),
                )
            },
            addresses = addresses.map { address ->
                val splitStreet = address.street?.split(" ")
                val street =
                    if (splitStreet != null && splitStreet.size > 1)
                        splitStreet.minus(splitStreet.lastOrNull { substr -> substr.all { it.isDigit() } })
                            .joinToString(" ")
                    else
                        address.street
                val streetNumber =
                    if (splitStreet != null && splitStreet.size > 1)
                        splitStreet.lastOrNull { substr -> substr.all { it.isDigit() } }?.toIntOrNull()
                    else
                        null
                Address(
                    street = street,
                    streetNumber = streetNumber,
                    city = address.city,
                    postcode = address.postcode?.toIntOrNull(),
                    region = address.region,
                    country = address.country,
                    type = Type.convertAddressType(
                        if (address.type == "custom") address.label ?: "" else address.type
                    ),
                )
            },
            websites = websites.map {
                de.benkralex.socius.data.model.Website(
                    value = it.url,
                    type = Type.convertWebsiteType(if (it.type == "custom") it.label ?: "" else it.type),
                )
            },
            relations = relations.map {
                de.benkralex.socius.data.model.Relation(
                    value = it.name,
                    type = Type.convertRelationType(if (it.type == "custom") it.label ?: "" else it.type),
                )
            },
            events = events
                .filter { it.day != null && it.month != null }
                .map {
                    de.benkralex.socius.data.model.Event(
                        day = it.day!!,
                        month = it.month!!,
                        year = it.year,
                        type = Type.convertEventType(if (it.type == "custom") it.label ?: "" else it.type),
                    )
                },
            groups = groups.map { it.name }.filter { it != null }.map { it!! },
            note = note,
            isStarred = isStarred,
            customFields = customFields,
        )
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

    fun getProfileBitmap(context: Context?): Bitmap? {
        if (photoBitmap != null) {
            return photoBitmap
        }
        if (thumbnailBitmap != null) {
            return thumbnailBitmap
        }
        if (photoUri != null && context != null) {
            try {
                context.contentResolver.openInputStream(photoUri!!.toUri())?.use { inputStream ->
                    return BitmapFactory.decodeStream(inputStream)
                }
            } catch (_: Exception) {}
        }
        if (thumbnailUri != null && context != null) {
            try {
                context.contentResolver.openInputStream(thumbnailUri!!.toUri())?.use { inputStream ->
                    return BitmapFactory.decodeStream(inputStream)
                }
            } catch (_: Exception) {}
        }
        return null
    }
}