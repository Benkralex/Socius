package de.benkralex.contacts.backend

import android.graphics.Bitmap

data class Contact(
    var id: String? = null,
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
    var sipAddresses: List<SipAddress> = emptyList(),
    var groups: List<Group> = emptyList(),
    var labels: List<String> = emptyList(),

    var customFields: Map<String, String> = emptyMap()
)

data class PhoneNumber(
    var number: String,
    var type: String, // mobile, home, work, etc.
    var label: String? = null
)

data class Email(
    var address: String,
    var type: String,
    var label: String? = null
)

data class PostalAddress(
    var street: String? = null,
    var city: String? = null,
    var region: String? = null,
    var postcode: String? = null,
    var country: String? = null,
    var type: String,
    var label: String? = null
)

data class IM(
    var protocol: String,
    var handle: String,
    var type: String,
    var label: String? = null
)

data class Website(
    var url: String,
    var type: String,
    var label: String? = null
)

data class Relation(
    var name: String,
    var type: String, // spouse, child, parent, etc.
    var label: String? = null
)

data class ContactEvent(
    var day: Int? = null,
    var month: Int? = null,
    var year: Int? = null,
    var type: String, // birthday, anniversary, etc.
    var label: String? = null
)

data class SipAddress(
    var address: String,
    var type: String,
    var label: String? = null
)

data class Group(
    var id: Long,
    var name: String?,
)