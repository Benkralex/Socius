package de.benkralex.socius.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Phone(
    var value: String = "",
    var type: Type.Phone = Type.Phone.HOME,
)
