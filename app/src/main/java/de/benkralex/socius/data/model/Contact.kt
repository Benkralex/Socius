package de.benkralex.socius.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Contact (
    var id: Int?,
    var origin: ContactOrigin,

    var name: Name = Name(),
    var job: Job = Job(),
    var profilePicture: ProfilePicture = ProfilePicture(),

    var phoneNumbers: List<Phone> = emptyList(),
    var emails: List<Email> = emptyList(),
    var addresses: List<Address> = emptyList(),
    var websites: List<Website> = emptyList(),
    var relations: List<Relation> = emptyList(),
    var events: List<Event> = emptyList(),
    var groups: List<String> = emptyList(),

    var note: String? = null,
    var isStarred: Boolean = false,
    var customFields: Map<String, String> = emptyMap()
)