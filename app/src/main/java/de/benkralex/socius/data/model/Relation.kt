package de.benkralex.socius.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Relation(
    var name: String,
    var type: String, // spouse, child, parent, etc.
    var label: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (other !is Relation) return false
        return name == other.name &&
                type == other.type &&
                label == other.label
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + (label?.hashCode() ?: 0)
        return result
    }
}