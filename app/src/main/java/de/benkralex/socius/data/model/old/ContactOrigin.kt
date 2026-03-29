package de.benkralex.socius.data.model.old

import kotlinx.serialization.Serializable

@Serializable
enum class ContactOrigin {
    LOCAL,
    IMPORT,
}