package de.benkralex.socius.pages.newContact

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import de.benkralex.socius.data.PostalAddress

class EditPostalAddressesState {
    val showFields by derivedStateOf { count > 0 }
    var count: Int by mutableIntStateOf(0)
    var streets: MutableList<MutableState<String>> by mutableStateOf(mutableListOf())
    var houseNumbers: MutableList<MutableState<String>> by mutableStateOf(mutableListOf())
    var cities: MutableList<MutableState<String>> by mutableStateOf(mutableListOf())
    var regions: MutableList<MutableState<String>> by mutableStateOf(mutableListOf())
    var postcodes: MutableList<MutableState<String>> by mutableStateOf(mutableListOf())
    var countries: MutableList<MutableState<String>> by mutableStateOf(mutableListOf())
    var types: MutableList<MutableState<String>> by mutableStateOf(mutableListOf())
    var labels: MutableList<MutableState<String>> by mutableStateOf(mutableListOf())

    fun hasRelevantData(): Boolean {
        return streets.any { it.value.isNotBlank() }
            || cities.any { it.value.isNotBlank() }
            || regions.any { it.value.isNotBlank() }
            || postcodes.any { it.value.isNotBlank() }
            || countries.any { it.value.isNotBlank() }
    }

    fun isRelevant(i: Int): Boolean {
        return streets[i].value.isNotBlank()
                || cities[i].value.isNotBlank()
                || regions[i].value.isNotBlank()
                || postcodes[i].value.isNotBlank()
                || countries[i].value.isNotBlank()
    }
    
    fun getRelevantData(): List<PostalAddress> {
        val addresses: MutableList<PostalAddress> = mutableListOf()
        for (i in 0..<count) {
            if (isRelevant(i)) {
                var street: String? = streets[i].value.trim().ifBlank { null }
                if (houseNumbers[i].value.isNotBlank() && street != null) {
                    street += " " + houseNumbers[i].value.trim()
                }

                addresses.add(
                    PostalAddress(
                        street = street,
                        city = cities[i].value.trim().ifBlank { null },
                        region = regions[i].value.trim().ifBlank { null },
                        postcode = postcodes[i].value.trim().ifBlank { null },
                        country = countries[i].value.trim().ifBlank { null },
                        type = types[i].value.trim().ifBlank { "home" },
                        label = labels[i].value.trim().ifBlank { null },
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
        postcodes.add(mutableStateOf(""))
        countries.add(mutableStateOf(""))
        types.add(mutableStateOf("home"))
        labels.add(mutableStateOf(""))
        count++
    }
}