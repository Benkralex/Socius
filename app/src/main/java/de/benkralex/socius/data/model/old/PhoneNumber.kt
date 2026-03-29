package de.benkralex.socius.data.model.old

import kotlinx.serialization.Serializable

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