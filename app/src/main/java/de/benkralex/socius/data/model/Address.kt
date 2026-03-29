package de.benkralex.socius.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    var street: String? = null,
    var streetNumber: Int? = null,
    var city: String? = null,
    var postcode: Int? = null,
    var region: String? = null,
    var country: String? = null,
    var extendedAddress: Extended? = null,
    var type: Type.Address = Type.Address.HOME
) {
    @Serializable
    data class Extended (
        var apartmentNumber: Int? = null,
        var suitNumber: Int? = null,
        var roomNumber: Int? = null,
        var floorNumber: Int? = null,
        var departmentNumber: Int? = null,
        var poBoxNumber: Int? = null,
    )

    fun format(): String {
        val addressParts = mutableListOf<String>()
        if (!street.isNullOrBlank()) {
            addressParts.add(listOfNotNull(street, streetNumber).joinToString(" "))
        }
        if (postcode != null || !city.isNullOrBlank()) {
            addressParts.add(listOfNotNull(postcode, city).joinToString(" "))
        }
        if (!region.isNullOrBlank() || !country.isNullOrBlank()) {
            addressParts.add(listOfNotNull(region, country).joinToString(", "))
        }
        return addressParts.joinToString("\n")
    }
}