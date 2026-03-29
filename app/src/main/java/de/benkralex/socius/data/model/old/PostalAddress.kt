package de.benkralex.socius.data.model.old

import kotlinx.serialization.Serializable

@Serializable
data class PostalAddress(
    var street: String? = null,
    var city: String? = null,
    var region: String? = null,
    var postcode: String? = null,
    var country: String? = null,
    var type: String,
    var label: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (other !is PostalAddress) return false
        return street == other.street &&
                city == other.city &&
                region == other.region &&
                postcode == other.postcode &&
                country == other.country &&
                type == other.type &&
                label == other.label
    }

    override fun hashCode(): Int {
        var result = street?.hashCode() ?: 0
        result = 31 * result + (city?.hashCode() ?: 0)
        result = 31 * result + (region?.hashCode() ?: 0)
        result = 31 * result + (postcode?.hashCode() ?: 0)
        result = 31 * result + (country?.hashCode() ?: 0)
        result = 31 * result + type.hashCode()
        result = 31 * result + (label?.hashCode() ?: 0)
        return result
    }
}