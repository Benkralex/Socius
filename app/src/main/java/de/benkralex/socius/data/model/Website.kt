package de.benkralex.socius.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Website(
    var url: String,
    var type: String,
    var label: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (other !is Website) return false
        return url == other.url &&
                type == other.type &&
                label == other.label
    }

    override fun hashCode(): Int {
        var result = url.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + (label?.hashCode() ?: 0)
        return result
    }
}