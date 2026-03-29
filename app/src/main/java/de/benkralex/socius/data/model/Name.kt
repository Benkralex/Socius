package de.benkralex.socius.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Name(
    var prefix: String? = null,
    var firstname: String? = null,
    var secondName: String? = null,
    var lastname: String? = null,
    var suffix: String? = null,
    var nickname: String? = null,
    var displayName: String? = null,
    var phoneticFirstname: String? = null,
    var phoneticSecondName: String? = null,
    var phoneticLastname: String? = null,
)
