package de.benkralex.socius.ui.components.editContact

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.text.isDigitsOnly
import de.benkralex.socius.data.model.Contact
import de.benkralex.socius.data.model.Address
import de.benkralex.socius.data.model.Type

class EditAddressesState {
    val showFields by derivedStateOf { count > 0 }
    var count: Int by mutableIntStateOf(0)
    var streets: MutableList<MutableState<String>> by mutableStateOf(mutableListOf())
    var houseNumbers: MutableList<MutableState<String>> by mutableStateOf(mutableListOf())
    var cities: MutableList<MutableState<String>> by mutableStateOf(mutableListOf())
    var regions: MutableList<MutableState<String>> by mutableStateOf(mutableListOf())
    var postcodes: MutableList<MutableState<Int?>> by mutableStateOf(mutableListOf())
    var countries: MutableList<MutableState<String>> by mutableStateOf(mutableListOf())
    var types: MutableList<MutableState<Type.Address>> by mutableStateOf(mutableListOf())

    fun hasRelevantData(): Boolean {
        return streets.any { it.value.isNotBlank() }
            || cities.any { it.value.isNotBlank() }
            || regions.any { it.value.isNotBlank() }
            || postcodes.any { it.value != null }
            || countries.any { it.value.isNotBlank() }
    }

    fun isRelevant(i: Int): Boolean {
        return streets[i].value.isNotBlank()
                || cities[i].value.isNotBlank()
                || regions[i].value.isNotBlank()
                || postcodes[i].value != null
                || countries[i].value.isNotBlank()
    }
    
    fun getRelevantData(): List<Address> {
        val addresses: MutableList<Address> = mutableListOf()
        for (i in 0..<count) {
            if (isRelevant(i)) {
                var street: String? = streets[i].value.trim().ifBlank { null }
                if (houseNumbers[i].value.isNotBlank() && street != null) {
                    street += " " + houseNumbers[i].value.trim()
                }

                addresses.add(
                    Address(
                        street = street,
                        city = cities[i].value.trim().ifBlank { null },
                        region = regions[i].value.trim().ifBlank { null },
                        postcode = postcodes[i].value,
                        country = countries[i].value.trim().ifBlank { null },
                        type = types[i].value,
                    )
                )
            }
        }
        return addresses
    }
    
    fun addNew() {
        streets.add(mutableStateOf(""))
        houseNumbers.add(mutableStateOf(""))
        cities.add(mutableStateOf(""))
        regions.add(mutableStateOf(""))
        postcodes.add(mutableStateOf(null))
        countries.add(mutableStateOf(""))
        types.add(mutableStateOf(Type.Address.HOME))
        count++
    }

    fun loadFromContact(contact: Contact) {
        for (address: Address in contact.addresses) {
            count++
            val houseNumber = address.street?.split(" ")?.first { it.isDigitsOnly() } ?: ""
            streets.add(mutableStateOf(address.street?.replace(" $houseNumber", "") ?: ""))
            houseNumbers.add(mutableStateOf(houseNumber))
            cities.add(mutableStateOf(address.city ?: ""))
            regions.add(mutableStateOf(address.region ?: ""))
            postcodes.add(mutableStateOf(address.postcode))
            countries.add(mutableStateOf(address.country ?: ""))
            types.add(mutableStateOf(address.type))
        }
    }

    fun reset() {
        count = 0
        streets.clear()
        houseNumbers.clear()
        cities.clear()
        regions.clear()
        postcodes.clear()
        countries.clear()
        types.clear()
    }
}