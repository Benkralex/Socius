package de.benkralex.socius.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Email(
    var value: String = "",
    var type: Type.Email = Type.Email.HOME,
)
