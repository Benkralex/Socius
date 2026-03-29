package de.benkralex.socius.data.model

import kotlinx.serialization.Serializable

@Serializable
enum class ContactOrigin {
    LOCAL,
    TEMPORARY,
    NEW,
}