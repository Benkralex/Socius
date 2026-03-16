package de.benkralex.socius.pages.newContact

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import de.benkralex.socius.data.model.Contact
import de.benkralex.socius.data.model.PhoneNumber

class EditPhoneNumbersState {
    val showFields by derivedStateOf { count > 0 }
    var count: Int by mutableIntStateOf(0)
    var numbers: MutableList<MutableState<String>> by mutableStateOf(mutableListOf())
    var types: MutableList<MutableState<String>> by mutableStateOf(mutableListOf())
    var labels: MutableList<MutableState<String>> by mutableStateOf(mutableListOf())

    fun hasRelevantData(): Boolean {
        return numbers.any { it.value.isNotBlank() }
    }

    fun isRelevant(i: Int): Boolean {
        return numbers[i].value.isNotBlank()
    }
    
    fun getRelevantData(): List<PhoneNumber> {
        val phoneNumbers: MutableList<PhoneNumber> = mutableListOf()
        for (i in 0..<count) {
            if (isRelevant(i)) {
                phoneNumbers.add(
                    PhoneNumber(
                        number = numbers[i].value.trim(),
                        type = types[i].value.trim().ifBlank { "home" },
                        label = labels[i].value.trim().ifBlank { null },
                    )
                )
            }
        }
        return phoneNumbers
    }

    fun addNew() {
        numbers.add(mutableStateOf(""))
        types.add(mutableStateOf("home"))
        labels.add(mutableStateOf(""))
        count++
    }

    fun loadFromContact(contact: Contact) {
        for (num: PhoneNumber in contact.phoneNumbers) {
            count++
            numbers.add(mutableStateOf(num.number))
            types.add(mutableStateOf(num.type))
            labels.add(mutableStateOf(num.label ?: ""))
        }
    }

    fun reset() {
        count = 0
        numbers.clear()
        types.clear()
        labels.clear()
    }
}