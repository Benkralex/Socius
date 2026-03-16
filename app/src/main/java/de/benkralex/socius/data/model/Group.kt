package de.benkralex.socius.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Group(
    var id: Long,
    var name: String?,
) {
    override fun equals(other: Any?): Boolean {
        if (other !is Group) return false
        return id == other.id &&
                name == other.name
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        return result
    }
}