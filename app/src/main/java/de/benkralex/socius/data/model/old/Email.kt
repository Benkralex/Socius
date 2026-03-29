package de.benkralex.socius.data.model.old

import kotlinx.serialization.Serializable

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