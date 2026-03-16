package de.benkralex.socius.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ContactEvent(
    var day: Int? = null,
    var month: Int? = null,
    var year: Int? = null,
    var type: String, // birthday, anniversary, etc.
    var label: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (other !is ContactEvent) return false
        return day == other.day &&
                month == other.month &&
                year == other.year &&
                type == other.type &&
                label == other.label
    }

    override fun hashCode(): Int {
        var result = day ?: 0
        result = 31 * result + (month ?: 0)
        result = 31 * result + (year ?: 0)
        result = 31 * result + type.hashCode()
        result = 31 * result + (label?.hashCode() ?: 0)
        return result
    }
}